package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Pergunta;
import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.PerguntaService;
import br.edu.ifpb.pweb2.xp.service.ResultadoService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/corridas/jogar")
public class JogoController {

    private static final String SESSAO_CORRIDA_ID = "corridaId";
    private static final String SESSAO_PERGUNTA_INDICE = "perguntaIndice";
    private static final String SESSAO_ACERTOS = "acertos";

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private ResultadoService resultadoService;

    @GetMapping
    public ModelAndView exibirPergunta(HttpSession session) {
        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);
        if (corridaId == null) {
            return new ModelAndView("redirect:/corridas");
        }

        if (tempoExpirado(session)) {
            return new ModelAndView("redirect:/corridas/jogar/fim");
        }

        List<Pergunta> perguntas = perguntaService.listarPorCorrida(corridaId);
        int indice = indiceAtual(session);

        if (perguntas.isEmpty()) {
            return new ModelAndView("redirect:/corridas/jogar/fim");
        }

        if (indice >= perguntas.size()) {
            return new ModelAndView("redirect:/corridas/jogar/fim");
        }

        Pergunta pergunta = perguntas.get(indice);
        ModelAndView model = new ModelAndView("corridas/jogar/pergunta");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("pergunta", pergunta);
        model.addObject("numero", indice + 1);
        model.addObject("total", perguntas.size());
        return model;
    }

    @PostMapping("/responder")
    public String responder(
            @RequestParam Long perguntaId,
            @RequestParam int resposta,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);
        if (corridaId == null) {
            return "redirect:/corridas";
        }

        if (tempoExpirado(session)) {
            return "redirect:/corridas/jogar/fim";
        }

        List<Pergunta> perguntas = perguntaService.listarPorCorrida(corridaId);
        int indice = indiceAtual(session);

        if (indice >= perguntas.size()) {
            return "redirect:/corridas/jogar/fim";
        }

        Pergunta perguntaAtual = perguntas.get(indice);
        if (!perguntaAtual.getId().equals(perguntaId)) {
            redirectAttributes.addFlashAttribute("erro", "Pergunta invalida para esta etapa da corrida.");
            return "redirect:/corridas/jogar";
        }

        boolean correta = perguntaService.validarResposta(perguntaAtual, resposta);
        if (correta) {
            session.setAttribute(SESSAO_ACERTOS, acertosAtuais(session) + 1);
            redirectAttributes.addFlashAttribute("feedbackCorreto", true);
            redirectAttributes.addFlashAttribute("feedbackMensagem", "Resposta correta!");
        } else {
            redirectAttributes.addFlashAttribute("feedbackCorreto", false);
            redirectAttributes.addFlashAttribute("feedbackMensagem", "Resposta incorreta.");
        }

        session.setAttribute(SESSAO_PERGUNTA_INDICE, indice + 1);

        if (indice + 1 >= perguntas.size()) {
            return "redirect:/corridas/jogar/fim";
        }

        return "redirect:/corridas/jogar";
    }

    @GetMapping("/fim")
    public ModelAndView fim(HttpSession session) {
        Long corridaId = (Long) session.getAttribute(SESSAO_CORRIDA_ID);
        String nomeUsuario = (String) session.getAttribute("usuario");

        if (corridaId == null || nomeUsuario == null) {
            return new ModelAndView("redirect:/corridas");
        }

        List<Pergunta> perguntas = perguntaService.listarPorCorrida(corridaId);
        int total = perguntas.size();
        int acertos = acertosAtuais(session);

        Corrida corrida = corridaService.buscarPorId(corridaId);

        try {
            BigDecimal pontuacaoBigDecimal = new BigDecimal(acertos);

            Participante participante = new Participante();
            participante.setNome(nomeUsuario);
            participante.setAdmin("admin".equalsIgnoreCase(nomeUsuario));

            resultadoService.salvar(participante, corrida, pontuacaoBigDecimal);

        } catch (Exception e) {
            System.err.println("Erro ao persistir o resultado no ranking: " + e.getMessage());
        }

        ModelAndView model = new ModelAndView("corridas/jogar/fim");
        model.addObject("corrida", corrida);
        model.addObject("total", total);
        model.addObject("acertos", acertos);
        model.addObject("tempoInicio", session.getAttribute("tempoInicioCorrida"));
        return model;
    }

    private int indiceAtual(HttpSession session) {
        Integer indice = (Integer) session.getAttribute(SESSAO_PERGUNTA_INDICE);
        return indice != null ? indice : 0;
    }

    private int acertosAtuais(HttpSession session) {
        Integer acertos = (Integer) session.getAttribute(SESSAO_ACERTOS);
        return acertos != null ? acertos : 0;
    }

    private boolean tempoExpirado(HttpSession session) {
        Integer tempoLimiteSegundos = (Integer) session.getAttribute("tempoLimiteSegundos");
        LocalDateTime tempoInicio = (LocalDateTime) session.getAttribute("tempoInicioCorrida");

        if (tempoLimiteSegundos == null || tempoInicio == null) {
            return false;
        }

        long segundosDecorridos = ChronoUnit.SECONDS.between(tempoInicio, LocalDateTime.now());
        return segundosDecorridos >= tempoLimiteSegundos;
    }   
}