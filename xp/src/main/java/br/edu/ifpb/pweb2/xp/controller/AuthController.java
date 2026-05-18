package br.edu.ifpb.pweb2.xp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @GetMapping
    public ModelAndView getForm(ModelAndView model) {
        model.setViewName("auth/login");
        return model;
    }

    @PostMapping("/login")
    public String login(String nome, HttpSession session) {
        // Lógica simples: se o nome digitado for "admin", ele ganha poderes de admin
        boolean isAdmin = "admin".equalsIgnoreCase(nome);
        
        // Guarda as informações na sessão para usar em outras telas (como o Lobby)
        session.setAttribute("usuario", nome);
        session.setAttribute("usuarioAdmin", isAdmin);

        // Redireciona para o seu Lobby de Corridas
        return "redirect:/corridas";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

    session.invalidate();

    return "redirect:/auth";
}

}

