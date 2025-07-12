package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.PublicacionDTO;
import org.springframework.data.domain.Page;

import com.piuraexpressa.model.dominio.Publicacion;

import java.util.Optional;

public interface PublicacionServicio {
    PublicacionDTO crearPublicacion(PublicacionDTO dto, String username);
    Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId);
    Optional<Publicacion> buscarPorId(Long id);
    // Puedes agregar más métodos según tu necesidad (editar, eliminar, etc.)
}
