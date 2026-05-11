package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.repository.CorridaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CorridaService {
    @Autowired
    private CorridaRepository repository;

    public List<Corrida> listarTodas() {
        return repository.findAll();
    }

    public void salvar(Corrida corrida) {
        repository.save(corrida);
    }
}
