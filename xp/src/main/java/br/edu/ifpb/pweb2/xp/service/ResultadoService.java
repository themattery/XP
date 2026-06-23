package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.exception.ValidationException;
import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.model.Resultado;
import br.edu.ifpb.pweb2.xp.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository repository;
    
    @Autowired
    private ParticipanteService participanteService;

    @Transactional
    public Resultado salvar(Participante participante, Corrida corrida, BigDecimal pontuacao) {
        if (participante == null || participante.getNome() == null) {
            throw new ValidationException("Dados do participante inválidos");
        }
        
        Participante participantePersistido = participanteService.buscarOuCriar(participante.getNome());
        
        Resultado resultado = new Resultado();
        resultado.setParticipante(participantePersistido);
        resultado.setCorrida(corrida);
        resultado.setPontuacao(pontuacao);
        resultado.setDataHora(LocalDateTime.now());
        return repository.save(resultado);
    }

    public Page<Resultado> rankingGeral(Pageable pageable) {
        return repository.findAllByOrderByPontuacaoDescDataHoraDesc(pageable);
    }

    public Page<Resultado> rankingPorCorrida(Long corridaId, Pageable pageable) {
        return repository.findRankingPorCorrida(corridaId, pageable);
    }

    public boolean existeResultadoPorCorrida(Long corridaId) {
        return repository.existsByCorridaId(corridaId);
    }

    public long contarResultadosPorCorrida(Long corridaId) {
        return repository.countByCorridaId(corridaId);
    }
}