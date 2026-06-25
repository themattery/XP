package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.config.UploadUtil;
import br.edu.ifpb.pweb2.xp.model.Pergunta;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.PerguntaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/corridas/{corridaId}/perguntas")
public class PerguntaController {

    private static final String SESSAO_ADMIN = "usuarioAdmin";

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listar(@PathVariable Long corridaId, HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/perguntas/lista");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("perguntas", perguntaService.listarPorCorrida(corridaId));
        return model;
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView formulario(@PathVariable Long corridaId, HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/perguntas/formulario");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("pergunta", new Pergunta());
        model.addObject("edicao", false);
        return model;
    }

    @GetMapping("/{perguntaId}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView editar(@PathVariable Long corridaId,
                               @PathVariable Long perguntaId,
                               HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        Pergunta pergunta = perguntaService.buscarPorId(perguntaId);
        if (!pergunta.getCorrida().getId().equals(corridaId)) {
            return new ModelAndView("redirect:/corridas/" + corridaId + "/perguntas");
        }

        ModelAndView model = new ModelAndView("corridas/perguntas/formulario");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("pergunta", pergunta);
        model.addObject("edicao", true);
        return model;
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(
            @PathVariable Long corridaId,
            @RequestParam(required = false) Long perguntaId,
            @RequestParam String enunciado,
            @RequestParam List<String> alternativas,
            @RequestParam Integer respostaCorreta,
            @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
            @RequestParam(value = "removerImagem", defaultValue = "false") boolean removerImagem,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        if (perguntaId == null) {
            String nomeImagem = UploadUtil.salvarImagem(imagemFile);
            perguntaService.salvar(corridaId, enunciado, alternativas, respostaCorreta, nomeImagem);
            redirectAttributes.addFlashAttribute("mensagem", "Pergunta cadastrada com sucesso.");
            return "redirect:/corridas/" + corridaId + "/perguntas/confirmar-pergunta";
        } else {
            perguntaService.atualizar(
                    perguntaId,
                    corridaId,
                    enunciado,
                    alternativas,
                    respostaCorreta,
                    imagemFile,
                    removerImagem);

            redirectAttributes.addFlashAttribute("mensagem", "Pergunta atualizada com sucesso.");
            return "redirect:/corridas/" + corridaId + "/perguntas";
        }
    }

    @GetMapping("/confirmar-pergunta")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView confirmar(@PathVariable Long corridaId, HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/perguntas/confirmar");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("modo", "pergunta");
        return model;
    }

    @PostMapping("/{perguntaId}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(
            @PathVariable Long corridaId,
            @PathVariable Long perguntaId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        perguntaService.excluir(perguntaId, corridaId);
        redirectAttributes.addFlashAttribute("mensagem", "Pergunta excluída com sucesso.");
        return "redirect:/corridas/" + corridaId + "/perguntas";
    }

    private String exigirAdmin(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSAO_ADMIN))) {
            return "redirect:/corridas";
        }
        return null;
    }

}
