package com.piuraexpressa.controller;

import com.piuraexpressa.dto.ComentarioDTO;
import com.piuraexpressa.servicio.ComentarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/publicaciones/{publicacionId}/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioServicio comentarioServicio;

    @PreAuthorize("hasAuthority('USUARIO_LISTAR_COMENTARIOS')")
    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> listarComentarios(@PathVariable Long publicacionId) {
        List<ComentarioDTO> comentarios = comentarioServicio.listarComentariosPorPublicacion(publicacionId);
        return ResponseEntity.ok(comentarios);
    }

    @PreAuthorize("hasAuthority('USUARIO_CREAR_COMENTARIO')")
    @PostMapping
    public ResponseEntity<ComentarioDTO> crearComentario(@PathVariable Long publicacionId,
                                                        @RequestBody ComentarioDTO dto,
                                                        Principal principal) {
        ComentarioDTO nuevoComentario = comentarioServicio.crearComentario(publicacionId, dto, principal.getName());
        return ResponseEntity.ok(nuevoComentario);
    }
}
