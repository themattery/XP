package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Resultado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    Page<Resultado> findAllByOrderByPontuacaoDescDataHoraDesc(Pageable pageable);
@Query("select r from Resultado r " +
       "where r.corrida.id = :corridaId " +
       "order by r.pontuacao desc, r.dataHora desc")
Page<Resultado> findRankingPorCorrida(
        @Param("corridaId") Long corridaId,
        Pageable pageable
);
}