package com.piuraexpressa.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
public void onAuthenticationSuccess(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication)
        throws IOException, ServletException {

    // Establecer manualmente el contexto de seguridad
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Guardar en la sesión para que Thymeleaf lo detecte
    HttpSession session = request.getSession(true);
    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                         SecurityContextHolder.getContext());

    // Lógica de redirección por permisos
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    String redirectUrl = "/";

    boolean isAdmin = authorities.stream()
            .anyMatch(ga -> ga.getAuthority().equals("MOSTRAR_PANEL_ADMIN"));

    if (isAdmin) {
        redirectUrl = "/admin/dashboard";
    }

    log.info("User '{}' logged in, redirecting to '{}'", authentication.getName(), redirectUrl);
    response.sendRedirect(redirectUrl);
}
}

