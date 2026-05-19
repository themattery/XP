package br.edu.ifpb.pweb2.xp.config;

<<<<<<< HEAD
import br.edu.ifpb.pweb2.xp.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
<<<<<<< HEAD
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/css/**", "/js/**", "/static/**", "/error", "/ranking/**", "/webjars/**");
    }
}
=======
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth",
                        "/auth/login",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}
>>>>>>> 2c5f8c3e5518d7ec191b7abaa2c7a70b26049878
