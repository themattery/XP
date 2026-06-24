package br.edu.ifpb.pweb2.xp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth",
                                "/auth/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/imagens-uploads/**",
                                "/webjars/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/corridas/novo",
                                "/corridas/*/editar",
                                "/corridas/*/perguntas/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/corridas/salvar",
                                "/corridas/*/excluir"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN", "PARTICIPANTE")
                .build();

        UserDetails participante = User.withUsername("participante")
                .password("{noop}participante")
                .roles("PARTICIPANTE")
                .build();

        return new InMemoryUserDetailsManager(admin, participante);
    }

    private void onAuthenticationSuccess(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            Authentication authentication) throws java.io.IOException {

        boolean admin = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        HttpSession session = request.getSession();
        session.setAttribute("usuario", authentication.getName());
        session.setAttribute("usuarioAdmin", admin);

        response.sendRedirect(request.getContextPath() + "/corridas");
    }
}
