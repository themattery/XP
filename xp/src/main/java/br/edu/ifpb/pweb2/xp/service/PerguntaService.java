package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import br.edu.ifpb.pweb2.xp.model.Pergunta;
import br.edu.ifpb.pweb2.xp.repository.PerguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerguntaService {

    @Autowired
    private PerguntaRepository repository;

    @Autowired
    private CorridaService corridaService;

    public List<Pergunta> listarPorCorrida(Long corridaId) {
        corridaService.buscarPorId(corridaId);
        return repository.findByCorridaIdOrderByIdAsc(corridaId);
    }

    public List<Pergunta> listarTodas() {
        return repository.findAll();
    }

    public Pergunta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pergunta não encontrada"));
    }

    public Pergunta salvar(Long corridaId, String enunciado, List<String> alternativas, Integer respostaCorreta) {
        Corrida corrida = corridaService.buscarPorId(corridaId);
        List<String> alternativasValidas = filtrarAlternativas(alternativas);
        validarDados(enunciado, alternativasValidas, respostaCorreta);

        Pergunta pergunta = new Pergunta();
        pergunta.setEnunciado(enunciado.trim());
        pergunta.setAlternativas(new ArrayList<>(alternativasValidas));
        pergunta.setRespostaCorreta(respostaCorreta);
        pergunta.setCorrida(corrida);
        return repository.save(pergunta);
    }

    @Transactional
    public void excluir(Long perguntaId, Long corridaId) {
        Pergunta pergunta = buscarPorId(perguntaId);
        if (!pergunta.getCorrida().getId().equals(corridaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pergunta não pertence a esta corrida");
        }
        repository.delete(pergunta);
    }

    public boolean validarResposta(Pergunta pergunta, int indiceEscolhido) {
        return pergunta.getRespostaCorreta() != null
                && pergunta.getRespostaCorreta() == indiceEscolhido;
    }

    private List<String> filtrarAlternativas(List<String> alternativas) {
        if (alternativas == null) {
            return List.of();
        }
        return alternativas.stream()
                .filter(a -> a != null && !a.isBlank())
                .map(String::trim)
                .toList();
    }

    private void validarDados(String enunciado, List<String> alternativas, Integer respostaCorreta) {
        if (enunciado == null || enunciado.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enunciado é obrigatório");
        }
        if (alternativas.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe pelo menos duas alternativas");
        }
        if (respostaCorreta == null || respostaCorreta < 0 || respostaCorreta >= alternativas.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecione a alternativa correta");
        }
    }

}
