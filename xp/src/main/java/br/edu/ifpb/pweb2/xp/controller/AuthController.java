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
        boolean isAdmin = "admin".equalsIgnoreCase(nome);
        
        session.setAttribute("usuario", nome);
        session.setAttribute("usuarioAdmin", isAdmin);

        return "redirect:/corridas";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}