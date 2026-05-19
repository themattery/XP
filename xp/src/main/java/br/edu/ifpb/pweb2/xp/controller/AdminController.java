package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.PerguntaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;

    @GetMapping("/dashboard")
    public ModelAndView dashboard(HttpSession session) {
        // Verificar se é admin
        Boolean isAdmin = (Boolean) session.getAttribute("usuarioAdmin");
        if (!Boolean.TRUE.equals(isAdmin)) {
            return new ModelAndView("redirect:/corridas");
        }

        ModelAndView model = new ModelAndView("admin/dashboard");
        model.addObject("corridas", corridaService.listarTodas());
        model.addObject("perguntas", perguntaService.listarTodas());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));
        return model;
    }
}
