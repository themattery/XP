package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    List<Pergunta> findByCorridaIdOrderByIdAsc(Long corridaId);

    long countByCorridaId(Long corridaId);

    void deleteByCorridaId(Long corridaId);

}
