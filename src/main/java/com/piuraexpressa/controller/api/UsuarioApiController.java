package com.piuraexpressa.controller.api;

import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioApiController {

    private final UsuarioServicio usuarioServicio;

    @GetMapping("/autenticado")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioAutenticado(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        return usuarioServicio.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
