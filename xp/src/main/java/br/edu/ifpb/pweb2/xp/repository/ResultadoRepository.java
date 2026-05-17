package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findAllByOrderByPontuacaoDescDataHoraDesc();

    @Query("select r from Resultado r where r.corrida.id = :corridaId order by r.pontuacao desc, r.dataHora desc")
    List<Resultado> findRankingPorCorrida(@Param("corridaId") Long corridaId);
}