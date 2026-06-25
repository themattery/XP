package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.exception.BusinessException;
import br.edu.ifpb.pweb2.xp.exception.NotFoundException;
import br.edu.ifpb.pweb2.xp.exception.ValidationException;
import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.repository.CorridaRepository;
import br.edu.ifpb.pweb2.xp.repository.PerguntaRepository;
import br.edu.ifpb.pweb2.xp.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CorridaService {

    @Autowired
    private CorridaRepository repository;

    @Autowired
    private PerguntaRepository perguntaRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    public List<Corrida> listarTodas() {
        return repository.findAll();
    }

    public List<Corrida> listarAtivas() {
        return repository.findByAtivaTrueOrAtivaIsNull();
    }

    public Corrida salvar(Corrida corrida) {
        // Validações básicas
        if (corrida.getNome() == null || corrida.getNome().trim().isEmpty()) {
            throw new ValidationException("O título da corrida é obrigatório");
        }
        if (corrida.getTempoLimiteSegundos() == null) {
            throw new ValidationException("O tempo limite é obrigatório");
        }
        if (corrida.getTempoLimiteSegundos() < 10) {
            throw new ValidationException("O tempo mínimo é de 10 segundos");
        }
        if (corrida.getTempoLimiteSegundos() > 600) {
            throw new ValidationException("O tempo máximo é de 600 segundos (10 minutos)");
        }
        
        if (corrida.getId() == null) {
            corrida.setAtiva(true);
            return repository.save(corrida);
        }

        Corrida existente = buscarPorId(corrida.getId());
        existente.setNome(corrida.getNome());
        existente.setTempoLimiteSegundos(corrida.getTempoLimiteSegundos());
        existente.setAtiva(corrida.getAtiva());
        return repository.save(existente);
    }

    public Corrida buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Corrida não encontrada com ID: " + id));
    }

    @Transactional
    public void excluir(Long id) {
        Corrida corrida = buscarPorId(id);
        
        try {
            resultadoRepository.deleteByCorridaId(id);
            perguntaRepository.deleteByCorridaId(id);
            repository.removerVinculosParticipantes(id);
            repository.delete(corrida);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(
                "Não foi possível excluir a corrida \"" + corrida.getNome() +
                "\" pois existem dados vinculados a ela."
            );
        }
    }

    @Transactional
    public void alternarAtiva(Long id) {
        Corrida corrida = buscarPorId(id);
        corrida.setAtiva(!corrida.getAtiva());
        repository.save(corrida);
    }

    public boolean possuiResultados(Long id) {
        return resultadoRepository.existsByCorridaId(id);
    }

    public long contarResultados(Long id) {
        return resultadoRepository.countByCorridaId(id);
    }
}
