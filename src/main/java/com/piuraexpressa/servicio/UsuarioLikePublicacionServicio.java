package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.UsuarioLikePublicacionDTO;

import java.util.List;

public interface UsuarioLikePublicacionServicio {
    UsuarioLikePublicacionDTO likePublicacion(Long usuarioId, Long publicacionId);

    void unlikePublicacion(Long usuarioId, Long publicacionId);

    long contarLikes(Long publicacionId);

    boolean usuarioHaDadoLike(Long usuarioId, Long publicacionId);

    List<UsuarioLikePublicacionDTO> obtenerLikesPorPublicacion(Long publicacionId);
}
