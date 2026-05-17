package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Corrida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CorridaRepository extends JpaRepository<Corrida, Long> {

    @Modifying
    @Query(value = "DELETE FROM participante_corridas WHERE corridas_id = :corridaId", nativeQuery = true)
    void removerVinculosParticipantes(@Param("corridaId") Long corridaId);

}
