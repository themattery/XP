package br.edu.ifpb.pweb2.xp.repository;

import br.edu.ifpb.pweb2.xp.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Optional<Participante> findByNome(String nome);
<<<<<<< HEAD
}
=======
}
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878
