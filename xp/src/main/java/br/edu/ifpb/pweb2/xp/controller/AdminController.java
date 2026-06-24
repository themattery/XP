package br.edu.ifpb.pweb2.xp.controller;

import br.edu.ifpb.pweb2.xp.model.Resultado;
import br.edu.ifpb.pweb2.xp.service.CorridaService;
import br.edu.ifpb.pweb2.xp.service.PerguntaService;
import br.edu.ifpb.pweb2.xp.service.ResultadoService;
import br.edu.ifpb.pweb2.xp.service.ParticipanteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CorridaService corridaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private ParticipanteService participanteService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView dashboard(HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("usuarioAdmin"))) {
            return new ModelAndView("redirect:/corridas");
        }

        ModelAndView model = new ModelAndView("admin/dashboard");
        
        model.addObject("totalCorridas", corridaService.listarTodas().size());
        model.addObject("totalPerguntas", perguntaService.listarTodas().size());
        model.addObject("totalParticipantes", participanteService.listarTodos().size());
        model.addObject("totalResultados", resultadoService.rankingGeral().size());
        
        model.addObject("corridas", corridaService.listarTodas());
        
        List<Resultado> resultados = resultadoService.rankingGeral();
        if (resultados.size() > 10) {
            resultados = resultados.subList(0, 10);
        }
        model.addObject("ultimosResultados", resultados);
        
        return model;
    }
}
