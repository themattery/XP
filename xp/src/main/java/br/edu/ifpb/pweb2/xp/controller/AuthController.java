package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private ParticipanteService participanteService;
    
    @GetMapping
    public ModelAndView getForm(ModelAndView model) {
        model.setViewName("auth/login");
        return model;
    }

    @PostMapping("/login")
    public String login(String nome, HttpSession session) {
        // Lógica simples: se o nome digitado for "admin", ele ganha poderes de admin
        boolean isAdmin = "admin".equalsIgnoreCase(nome);
        
        // Busca ou cria o participante no banco
        Participante participante = participanteService.buscarOuCriar(nome, isAdmin);
        
        // Guarda as informações na sessão para usar em outras telas (como o Lobby)
        session.setAttribute("usuario", nome);
        session.setAttribute("usuarioAdmin", isAdmin);
        session.setAttribute("participanteId", participante.getId());

        // Redireciona para o seu Lobby de Corridas
        return "redirect:/corridas";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
<<<<<<< HEAD
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/auth";
    }
=======

    session.invalidate();

    return "redirect:/auth";
}
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878

}

