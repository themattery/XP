package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ranking")
public class RankingController {

    private static final int TAMANHO_PAGINA = 10;

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping
    public ModelAndView rankingGeral(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session) {

        ModelAndView model = new ModelAndView("ranking/geral");

        Page<?> resultados =
                resultadoService.rankingGeral(
                        PageRequest.of(page, TAMANHO_PAGINA));

        model.addObject("resultados", resultados);
        model.addObject("currentPage", page);
        model.addObject("totalPages", resultados.getTotalPages());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    @GetMapping("/corrida/{corridaId}")
    public ModelAndView rankingPorCorrida(
            @PathVariable Long corridaId,
            @RequestParam(defaultValue = "0") int page,
            HttpSession session) {

        ModelAndView model = new ModelAndView("ranking/corrida");

        Page<?> resultados =
                resultadoService.rankingPorCorrida(
                        corridaId,
                        PageRequest.of(page, TAMANHO_PAGINA));

        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("resultados", resultados);
        model.addObject("currentPage", page);
        model.addObject("totalPages", resultados.getTotalPages());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }
}