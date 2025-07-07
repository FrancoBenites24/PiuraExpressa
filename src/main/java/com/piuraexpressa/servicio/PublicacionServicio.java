package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.PublicacionDTO;
import org.springframework.data.domain.Page;

public interface PublicacionServicio {
    PublicacionDTO crearPublicacion(PublicacionDTO dto, String username);
    Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId);
    // Puedes agregar más métodos según tu necesidad (editar, eliminar, etc.)
}
