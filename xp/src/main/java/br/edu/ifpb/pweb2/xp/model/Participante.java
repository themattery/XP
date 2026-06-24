package br.edu.ifpb.pweb2.xp.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;



@Data
@Entity
public class Participante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String senha;

    private Boolean admin;

    @ManyToMany
    private List<Corrida> corridas;

}
