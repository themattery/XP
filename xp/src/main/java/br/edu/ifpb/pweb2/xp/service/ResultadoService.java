package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.model.Resultado;
import br.edu.ifpb.pweb2.xp.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository repository;

    public Resultado salvar(Participante participante, Corrida corrida, BigDecimal pontuacao) {
        Resultado resultado = new Resultado();
        resultado.setParticipante(participante);
        resultado.setCorrida(corrida);
        resultado.setPontuacao(pontuacao);
        resultado.setDataHora(LocalDateTime.now());
        return repository.save(resultado);
    }

    public List<Resultado> rankingGeral() {
        return repository.findAllByOrderByPontuacaoDescDataHoraDesc();
    }

    public List<Resultado> rankingPorCorrida(Long corridaId) {
        return repository.findRankingPorCorrida(corridaId);
    }
}