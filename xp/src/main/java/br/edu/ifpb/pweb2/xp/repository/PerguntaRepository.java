package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

    List<Pergunta> findByCorridaIdOrderByIdAsc(Long corridaId);

    long countByCorridaId(Long corridaId);

    @Modifying
    @Query(value = """
            DELETE FROM pergunta_alternativas
            WHERE pergunta_id IN (
                SELECT id FROM pergunta WHERE corrida_id = :corridaId
            )
            """, nativeQuery = true)
    void deleteAlternativasByCorridaId(@Param("corridaId") Long corridaId);

    void deleteByCorridaId(Long corridaId);

}
