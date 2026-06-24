package br.edu.ifpb.pweb2.xp.service;

import br.edu.ifpb.pweb2.xp.model.Participante;
import br.edu.ifpb.pweb2.xp.repository.ParticipanteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ParticipanteUserDetailsService implements UserDetailsService {

    private final ParticipanteRepository repository;

    public ParticipanteUserDetailsService(ParticipanteRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Participante participante = repository.findByNomeIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado: " + username));

        String[] roles = Boolean.TRUE.equals(participante.getAdmin())
                ? new String[]{"ADMIN", "PARTICIPANTE"}
                : new String[]{"PARTICIPANTE"};

        return User.withUsername(participante.getNome())
                .password(participante.getSenha())
                .roles(roles)
                .build();
    }
}
