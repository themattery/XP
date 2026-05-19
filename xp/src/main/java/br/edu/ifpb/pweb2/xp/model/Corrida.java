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

<<<<<<< HEAD
    private String descricao;

    private Integer tempoLimitadoSegundos;
=======
    private Integer tempoLimiteSegundos;
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878

}
