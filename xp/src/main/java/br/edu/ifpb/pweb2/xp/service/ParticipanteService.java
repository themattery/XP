package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository repository;

    public Participante buscarOuCriar(String nome) {
        Optional<Participante> existente = repository.findByNome(nome);
        
        if (existente.isPresent()) {
            return existente.get();
        }
        
        Participante novo = new Participante();
        novo.setNome(nome);
        novo.setAdmin("admin".equalsIgnoreCase(nome));
        return repository.save(novo);
    }
    public List<Participante> listarTodos() {
    return repository.findAll();
}
}