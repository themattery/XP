package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
import java.util.Optional;

>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878
@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository repository;

<<<<<<< HEAD
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
=======
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
}
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878
