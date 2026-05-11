package br.edu.ifpb.pweb2.xp.controller;

import org.springframework.web.bind.annotation.PostMapping;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller 
@RequestMapping("/corridas")
public class CorridaController {
    
    @Autowired
    private CorridaService service;

    @GetMapping
    public ModelAndView lobby(ModelAndView model) {

        // Adiciona a lista de corridas para ser usada no HTML
        model.addObject("corridas", service.listarTodas());
        model.setViewName("corridas/lobby");
        return model;
    }

    @GetMapping("/novo")
    public ModelAndView formularioCadastro(ModelAndView model) {
        model.addObject("corrida", new Corrida()); // Para o formulário de cadastro
        model.setViewName("corridas/formulario");
        return model;
    }

    @PostMapping("/salvar")
    public String salvar(Corrida corrida) {
        service.salvar(corrida);
        return "redirect:/corridas";
    }
}
