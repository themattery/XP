package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository repository;

    public Participante buscarOuCriar(String nome, boolean admin) {
        return repository.findByNome(nome)
                .orElseGet(() -> {
                    Participante novo = new Participante();
                    novo.setNome(nome);
                    novo.setAdmin(admin);
                    return repository.save(novo);
                });
    }

    public Participante buscarPorNome(String nome) {
        return repository.findByNome(nome)
                .orElse(null);
    }
}
