package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.repository.CorridaRepository;
import br.edu.ifpb.pweb2.xp.repository.PerguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CorridaService {

    @Autowired
    private CorridaRepository repository;

    @Autowired
    private PerguntaRepository perguntaRepository;

    public List<Corrida> listarTodas() {
        return repository.findAll();
    }

    public Corrida salvar(Corrida corrida) {
        if (corrida.getNome() == null || corrida.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome da corrida é obrigatório");
        }
        return repository.save(corrida);
    }

    public Corrida buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Corrida não encontrada"));
    }

    @Transactional
    public void excluir(Long id) {
        buscarPorId(id);
        perguntaRepository.deleteByCorridaId(id);
        repository.removerVinculosParticipantes(id);
        repository.deleteById(id);
    }

}
