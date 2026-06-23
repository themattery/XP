package br.edu.ifpb.pweb2.xp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;

    @ElementCollection
    private List<String> alternativas;

    private Integer respostaCorreta;

    @ManyToOne
    @JoinColumn(name = "corrida_id")
    private Corrida corrida;

    private String imagem;

}
