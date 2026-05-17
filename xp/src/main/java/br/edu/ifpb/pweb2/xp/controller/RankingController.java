package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping
    public ModelAndView rankingGeral(HttpSession session) {
        ModelAndView model = new ModelAndView("ranking/geral");
        model.addObject("resultados", resultadoService.rankingGeral());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));
        return model;
    }

    @GetMapping("/corrida/{corridaId}")
    public ModelAndView rankingPorCorrida(@PathVariable Long corridaId, HttpSession session) {
        ModelAndView model = new ModelAndView("ranking/corrida");
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("resultados", resultadoService.rankingPorCorrida(corridaId));
        model.addObject("usuarioLogado", session.getAttribute("usuario"));
        return model;
    }
}