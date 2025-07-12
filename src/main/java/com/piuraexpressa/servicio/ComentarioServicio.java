package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.ComentarioDTO;

import java.util.List;

public interface ComentarioServicio {
    long contarComentariosPorPublicacion(Long publicacionId);
    List<ComentarioDTO> listarComentariosPorPublicacion(Long publicacionId);
    ComentarioDTO crearComentario(Long publicacionId, ComentarioDTO dto, String username);
}
