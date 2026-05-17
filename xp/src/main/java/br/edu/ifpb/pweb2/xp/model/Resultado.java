package br.edu.ifpb.pweb2.xp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Participante participante;

    @ManyToOne(optional = false)
    private Corrida corrida;

    private BigDecimal pontuacao;

    private LocalDateTime dataHora;
}