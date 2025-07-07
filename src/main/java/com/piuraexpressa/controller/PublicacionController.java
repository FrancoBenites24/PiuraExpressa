package com.piuraexpressa.controller;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioLikePublicacionDTO;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.UsuarioLikePublicacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionServicio publicacionServicio;
    private final UsuarioLikePublicacionServicio UsuarioLikePublicacionServicio;

    @PostMapping
    public PublicacionDTO crearPublicacion(@RequestBody PublicacionDTO dto, Principal principal) {
        return publicacionServicio.crearPublicacion(dto, principal.getName());
    }

    /**
     * Obtener publicaciones paginadas con filtros y búsqueda.
     */
    @GetMapping
    public ResponseEntity<?> listarPublicaciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long usuarioId) {
        // Ajusta los filtros según tu servicio
        Page<PublicacionDTO> publicaciones = publicacionServicio.buscarPublicaciones(page, size, sort, search, usuarioId);
        return ResponseEntity.ok(publicaciones);
    }

    /**
     * Dar like a una publicación
     */
    @PostMapping("/{publicacionId}/like")
    public ResponseEntity<UsuarioLikePublicacionDTO> likePublicacion(
            @PathVariable Long publicacionId,
            @RequestParam Long usuarioId) {
        UsuarioLikePublicacionDTO dto = UsuarioLikePublicacionServicio.likePublicacion(usuarioId, publicacionId);
        long totalLikes = UsuarioLikePublicacionServicio.contarLikes(publicacionId);
        dto.setFechaLike(dto.getFechaLike()); // Para asegurar formato correcto si se necesita
        return ResponseEntity.ok(dto);
    }

    /**
     * Quitar like a una publicación
     */
    @PostMapping("/{publicacionId}/unlike")
    public ResponseEntity<?> unlikePublicacion(
            @PathVariable Long publicacionId,
            @RequestParam Long usuarioId) {
        UsuarioLikePublicacionServicio.unlikePublicacion(usuarioId, publicacionId);
        long totalLikes = UsuarioLikePublicacionServicio.contarLikes(publicacionId);
        return ResponseEntity.ok(Map.of("totalLikes", totalLikes));
    }

    /**
     * Obtener cantidad total de likes para una publicación
     */
    @GetMapping("/{publicacionId}/likes")
    public ResponseEntity<Long> contarLikes(
            @PathVariable Long publicacionId) {
        long total = UsuarioLikePublicacionServicio.contarLikes(publicacionId);
        return ResponseEntity.ok(total);
    }

    /**
     * Saber si un usuario ya dio like a una publicación
     */
    @GetMapping("/{publicacionId}/liked-by")
    public ResponseEntity<Boolean> usuarioHaDadoLike(
            @PathVariable Long publicacionId,
            @RequestParam Long usuarioId) {
        boolean liked = UsuarioLikePublicacionServicio.usuarioHaDadoLike(usuarioId, publicacionId);
        return ResponseEntity.ok(liked);
    }

    // Otros endpoints como crear, editar, eliminar publicaciones, comentarios, etc.
    // Los agregas según lo necesites.
}
