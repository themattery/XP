package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Pergunta;
import br.edu.ifpb.pweb2.xp.repository.PerguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository repository;

    public List<Pergunta> listarPorCorrida(Long corridaId) {
        return repository.findByCorridaIdOrderByIdAsc(corridaId);
    }

    public Pergunta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pergunta não encontrada"));
    }

    public boolean validarResposta(Pergunta pergunta, int indiceEscolhido) {
        return pergunta.getRespostaCorreta() != null
                && pergunta.getRespostaCorreta() == indiceEscolhido;
    }

}
