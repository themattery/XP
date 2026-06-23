package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.ResultadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        ModelAndView model = new ModelAndView("ranking/geral");

        size = validarTamanhoPagina(size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Resultado> resultadosPage = resultadoService.rankingGeral(pageable);

        model.addObject("resultados", resultadosPage.getContent());
        model.addObject("currentPage", page);
        model.addObject("pageSize", size);
        model.addObject("totalPages", resultadosPage.getTotalPages());
        model.addObject("totalItems", resultadosPage.getTotalElements());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    @GetMapping("/corrida/{corridaId}")
    public ModelAndView rankingPorCorrida(
            @PathVariable Long corridaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        ModelAndView model = new ModelAndView("ranking/corrida");

        size = validarTamanhoPagina(size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Resultado> resultadosPage = resultadoService.rankingPorCorrida(corridaId, pageable);

        model.addObject("corrida", corridaService.buscarPorId(corridaId));
        model.addObject("resultados", resultadosPage.getContent());
        model.addObject("currentPage", page);
        model.addObject("pageSize", size);
        model.addObject("totalPages", resultadosPage.getTotalPages());
        model.addObject("totalItems", resultadosPage.getTotalElements());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    private int validarTamanhoPagina(int size) {
        List<Integer> tamanhosPermitidos = List.of(5, 10, 20, 50);
        return tamanhosPermitidos.contains(size) ? size : 10;
    }
}