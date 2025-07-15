package com.piuraexpressa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.piuraexpressa.config.seguridad.DynamicPermissionFilter;
import com.piuraexpressa.config.seguridad.JwtAuthenticationFilter;
import com.piuraexpressa.config.seguridad.UsuarioDetallesServicio;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioDetallesServicio usuarioDetallesServicio;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final com.piuraexpressa.config.CustomPermissionEvaluator customPermissionEvaluator;
    private final DynamicPermissionFilter dynamicPermissionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/admin/provincias/guardar"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**","/error/**").permitAll()
                        .requestMatchers("/eventos/**").authenticated()
                        // Revert to authenticated() to fix access() method error
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/login?logout").permitAll())
                // denegar permisos acciones alertas
                .exceptionHandling(ex -> ex.accessDeniedHandler(accesoDenegadoJsonHandler()))

                .userDetailsService(usuarioDetallesServicio)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(dynamicPermissionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }

    // mensajes personalziados por accesodenegado ymmm
    @Bean
    public AccessDeniedHandler accesoDenegadoJsonHandler() {
        return (request, response, accessDeniedException) -> {
            String uri = request.getRequestURI();

            if (uri.startsWith("/api/publicaciones") && uri.contains("/comentarios")) {
                // comportamiento para comentarios (403 con JSON)
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"mensaje\": \"No tienes permiso para comentar\"}");
            } else {
                // redirigir a página personalizada 403.html para todo lo demás
            request.getRequestDispatcher("/error/403").forward(request, response);
            }
        };
    }

}
