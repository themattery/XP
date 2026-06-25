package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Resultado;
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

    private static final int TAMANHO_PADRAO = 10;
    private static final List<Integer> TAMANHOS_PRE_DEFINIDOS = List.of(2, 5, 10, 20);

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
        model.addObject("pageSizeOptions", TAMANHOS_PRE_DEFINIDOS);
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
        model.addObject("pageSizeOptions", TAMANHOS_PRE_DEFINIDOS);
        model.addObject("totalPages", resultadosPage.getTotalPages());
        model.addObject("totalItems", resultadosPage.getTotalElements());
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    private int validarTamanhoPagina(int size) {
        return size > 0 && size <= 100 ? size : TAMANHO_PADRAO;
    }
}
