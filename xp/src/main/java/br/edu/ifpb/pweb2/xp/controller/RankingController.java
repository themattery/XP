<<<<<<< HEAD
=======
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

>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
@Controller
@RequestMapping("/ranking")
public class RankingController {

<<<<<<< HEAD
    private static final int PAGE_SIZE = 10;

=======
    private static final int TAMANHO_PAGINA = 10;

>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private CorridaService corridaService;

    @GetMapping
<<<<<<< HEAD
    public ModelAndView rankingGeral(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        
=======
    public ModelAndView rankingGeral(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session) {

>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
        ModelAndView model = new ModelAndView("ranking/geral");
<<<<<<< HEAD
        
        size = validarTamanhoPagina(size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Resultado> resultadosPage = resultadoService.rankingGeral(pageable);
        
        model.addObject("resultados", resultadosPage.getContent());
        model.addObject("currentPage", page);
        model.addObject("pageSize", size);
        model.addObject("totalPages", resultadosPage.getTotalPages());
        model.addObject("totalItems", resultadosPage.getTotalElements());
=======

        Page<?> resultados =
                resultadoService.rankingGeral(
                        PageRequest.of(page, TAMANHO_PAGINA));

        model.addObject("resultados", resultados);
        model.addObject("currentPage", page);
        model.addObject("totalPages", resultados.getTotalPages());
>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    @GetMapping("/corrida/{corridaId}")
<<<<<<< HEAD
    public ModelAndView rankingPorCorrida(
            @PathVariable Long corridaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        
=======
    public ModelAndView rankingPorCorrida(
            @PathVariable Long corridaId,
            @RequestParam(defaultValue = "0") int page,
            HttpSession session) {

>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
        ModelAndView model = new ModelAndView("ranking/corrida");
<<<<<<< HEAD
        
        size = validarTamanhoPagina(size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Resultado> resultadosPage = resultadoService.rankingPorCorrida(corridaId, pageable);
        
=======

        Page<?> resultados =
                resultadoService.rankingPorCorrida(
                        corridaId,
                        PageRequest.of(page, TAMANHO_PAGINA));

>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
        model.addObject("corrida", corridaService.buscarPorId(corridaId));
<<<<<<< HEAD
        model.addObject("resultados", resultadosPage.getContent());
        model.addObject("currentPage", page);
        model.addObject("pageSize", size);
        model.addObject("totalPages", resultadosPage.getTotalPages());
        model.addObject("totalItems", resultadosPage.getTotalElements());
=======
        model.addObject("resultados", resultados);
        model.addObject("currentPage", page);
        model.addObject("totalPages", resultados.getTotalPages());
>>>>>>> 9eeb54c01e920970f1eb0f15f28392aa43bd5919
        model.addObject("usuarioLogado", session.getAttribute("usuario"));

        return model;
    }

    private int validarTamanhoPagina(int size) {
        List<Integer> tamanhosPermitidos = List.of(5, 10, 20, 50);
        if (tamanhosPermitidos.contains(size)) {
            return size;
        }
        return 10;
    }
}