package com.piuraexpressa.config.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that checks dynamic permissions for each request URL using the CustomPermissionEvaluator.
 */
@Component
public class DynamicPermissionFilter extends OncePerRequestFilter {

    private final PermissionEvaluator permissionEvaluator;

    // URLs públicas que no requieren permiso
    private static final String[] PUBLIC_URLS = {
            "/auth/",
            "/css/",
            "/js/",
            "/images/",
            "/favicon.ico",
            "/",
            "/index",
            "/home"
    };

    public DynamicPermissionFilter(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    private boolean isPublicUrl(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isPublicUrl(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Map URL to permission name
            String permissionName = mapUrlToPermissionName(request.getRequestURI());
            boolean hasPermission = permissionEvaluator.hasPermission(authentication, request.getRequestURI(), permissionName);
            if (!hasPermission) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }
    }

    private String mapUrlToPermissionName(String url) {
        if (url.startsWith("/admin/dashboard")) {
            return "MOSTRAR_PANEL_ADMIN";
        } else if (url.startsWith("/admin/provincias/editar")) {
            return "ADMIN_PROVINCIAS_EDITAR";
        } else if (url.startsWith("/admin/usuarios")) {
            return "ADMIN_USUARIOS_ACCEDER";
        }
        // Default permission if no specific mapping
        return "ACCESS";
    }
}
