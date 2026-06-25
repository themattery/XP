package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.PerguntaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/corridas")
public class CorridaController {

    private static final String SESSAO_ADMIN = "usuarioAdmin";

    @Autowired
    private CorridaService service;

    @Autowired
    private PerguntaService perguntaService;

    @GetMapping
    public ModelAndView lobby(ModelAndView model, HttpSession session) {
        model.addObject("corridas", service.listarTodas());
        model.addObject("usuario", session.getAttribute("usuario"));
        model.setViewName("corridas/lobby");
        return model;
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView formularioCadastro(HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/formulario");
        model.addObject("corrida", new Corrida());
        model.addObject("edicao", false);
        return model;
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView formularioEdicao(@PathVariable Long id, HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/formulario");
        model.addObject("corrida", service.buscarPorId(id));
        model.addObject("edicao", true);
        return model;
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(
            @Valid Corrida corrida,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            org.springframework.ui.Model model) {
        
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        if (result.hasErrors()) {
            model.addAttribute("corrida", corrida);
            model.addAttribute("edicao", corrida.getId() != null);
            return "corridas/formulario";
        }

        try {
            boolean nova = corrida.getId() == null;
            Corrida corridaSalva = service.salvar(corrida);
            redirectAttributes.addFlashAttribute("mensagem",
                    nova ? "Corrida cadastrada com sucesso!" : "Corrida atualizada com sucesso!");
            return nova ? "redirect:/corridas/" + corridaSalva.getId() + "/perguntas/confirmar-corrida" : "redirect:/corridas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar corrida: " + e.getMessage());
            return "redirect:/corridas/novo";
        }
    }

    @GetMapping("/{id}/perguntas/confirmar-corrida")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView confirmarCadastroPerguntas(@PathVariable Long id, HttpSession session) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return new ModelAndView(redirect);
        }

        ModelAndView model = new ModelAndView("corridas/perguntas/confirmar");
        model.addObject("corrida", service.buscarPorId(id));
        model.addObject("modo", "corrida");
        return model;
    }

    @PostMapping("/{id}/excluir")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        try {
            Corrida corrida = service.buscarPorId(id);
            service.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Corrida \"" + corrida.getNome() + "\" excluída com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/corridas";
    }

    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Corrida corrida = service.buscarPorId(id);
            
            // Verificar se a corrida está ativa
            if (Boolean.FALSE.equals(corrida.getAtiva())) {
                redirectAttributes.addFlashAttribute("erro", "Esta corrida não está disponível no momento!");
                return "redirect:/corridas";
            }
            
            // Verificar se a corrida tem perguntas
            long totalPerguntas = perguntaService.contarPorCorrida(id);
            if (totalPerguntas == 0) {
                redirectAttributes.addFlashAttribute("erro", "Esta corrida não possui perguntas cadastradas!");
                return "redirect:/corridas/" + id;
            }

            session.setAttribute("corridaId", id);
            session.setAttribute("tempoInicioCorrida", LocalDateTime.now());
            session.setAttribute("perguntaIndice", 0);
            session.setAttribute("acertos", 0);
            session.setAttribute("tempoLimiteSegundos", corrida.getTempoLimiteSegundos());
            session.setAttribute("totalPerguntas", totalPerguntas);

            redirectAttributes.addFlashAttribute("mensagem", 
                "Corrida \"" + corrida.getNome() + "\" iniciada! Você tem " +
                corrida.getTempoLimiteSegundos() + " segundos para responder " + totalPerguntas + " perguntas. Boa sorte!");
            return "redirect:/corridas/jogar";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", " Erro ao iniciar corrida: " + e.getMessage());
            return "redirect:/corridas";
        }
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id) {
        return "redirect:/corridas/" + id + "/perguntas";
    }

    private String exigirAdmin(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSAO_ADMIN))) {
            return "redirect:/corridas";
        }
        return null;
    }
}
