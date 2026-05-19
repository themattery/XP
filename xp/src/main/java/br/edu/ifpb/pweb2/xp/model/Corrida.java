package br.edu.ifpb.pweb2.xp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Corrida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer tempoLimiteSegundos;

}
