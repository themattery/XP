package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping
    public ModelAndView lobby(ModelAndView model) {
        model.addObject("corridas", service.listarTodas());
        model.setViewName("corridas/lobby");
        return model;
    }

    @GetMapping("/novo")
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
    public String salvar(Corrida corrida, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        boolean nova = corrida.getId() == null;
        service.salvar(corrida);
        redirectAttributes.addFlashAttribute("mensagem",
                nova ? "Corrida cadastrada com sucesso." : "Corrida atualizada com sucesso.");
        return "redirect:/corridas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = exigirAdmin(session);
        if (redirect != null) {
            return redirect;
        }

        service.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Corrida excluída com sucesso.");
        return "redirect:/corridas";
    }

    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id, HttpSession session) {
        service.buscarPorId(id);

        session.setAttribute("corridaId", id);
        session.setAttribute("tempoInicioCorrida", LocalDateTime.now());
        session.setAttribute("perguntaIndice", 0);
        session.setAttribute("acertos", 0);

        return "redirect:/corridas/jogar";
    }

    @GetMapping("/{id}")
    public ModelAndView detalhe(@PathVariable Long id, ModelAndView model) {
        model.setViewName("corridas/detalhe");
        model.addObject("corrida", service.buscarPorId(id));
        return model;
    }

    private String exigirAdmin(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute(SESSAO_ADMIN))) {
            return "redirect:/corridas";
        }
        return null;
    }

}
