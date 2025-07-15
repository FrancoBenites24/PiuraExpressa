package com.piuraexpressa.controller;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.dto.UsuarioLikePublicacionDTO;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.ReportePublicacionServicio;
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
    private final ReportePublicacionServicio reportePublicacionServicio;

    @PostMapping
    public ResponseEntity<PublicacionDTO> crearPublicacion(@RequestBody PublicacionDTO dto, Principal principal) {
        try {
            PublicacionDTO created = publicacionServicio.crearPublicacion(dto, principal.getName());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(null);
        }
    }


    @PutMapping("/{id}")
    public PublicacionDTO actualizarPublicacion(@PathVariable Long id, @RequestBody PublicacionDTO dto,
            Principal principal) {
        return publicacionServicio.actualizarPublicacion(id, dto, principal.getName());
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
            @RequestParam(required = false) Long usuarioId,
            Principal principal) {
        // Obtener id del usuario autenticado
        Long idUsuarioAutenticado = null;
        boolean isAdmin = false;

        if (principal != null) {
            try {
                UsuarioDTO usuario = publicacionServicio.buscarUsuarioPorUsername(principal.getName())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                idUsuarioAutenticado = usuario.getId();
                isAdmin = usuario.getRoles() != null && usuario.getRoles().contains("ROLE_ADMIN");
            } catch (Exception e) {
                idUsuarioAutenticado = null;
                isAdmin = false;
            }
        }

        // Ajusta los filtros según tu servicio
        Page<PublicacionDTO> publicaciones = publicacionServicio.buscarPublicaciones(page, size, sort, search,
                usuarioId, idUsuarioAutenticado, false);
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

    @PostMapping("/{publicacionId}/reportar")
    public ResponseEntity<?> reportarPublicacion(@PathVariable Long publicacionId, Principal principal) {
        try {
            Long usuarioId = obtenerIdUsuarioPorPrincipal(principal);
            reportePublicacionServicio.reportarPublicacion(publicacionId, usuarioId);
            return ResponseEntity.ok(Map.of("message", "Publicación reportada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error al reportar publicación"));
        }
    }

    private Long obtenerIdUsuarioPorPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return publicacionServicio.obtenerIdUsuarioPorUsername(principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Long id, Principal principal) {
        try {
            publicacionServicio.eliminarPublicacion(id, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Publicación eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        }
    }
}
