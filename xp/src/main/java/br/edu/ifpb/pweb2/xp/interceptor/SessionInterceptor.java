package br.edu.ifpb.pweb2.xp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // permitir caminhos públicos
        if (uri.startsWith("/auth") || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/static") || uri.startsWith("/ranking") || uri.startsWith("/error") || uri.startsWith("/webjars")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            return true;
        }

        response.sendRedirect("/auth");
        return false;
    }

}
