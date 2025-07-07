package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.UsuarioLikePublicacion;
import com.piuraexpressa.model.dominio.UsuarioLikePublicacion.UsuarioLikePublicacionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioLikePublicacionRepositorio extends JpaRepository<UsuarioLikePublicacion, UsuarioLikePublicacionId> {
    List<UsuarioLikePublicacion> findByIdPublicacionId(Long publicacionId);
    Optional<UsuarioLikePublicacion> findByIdUsuarioIdAndIdPublicacionId(Long usuarioId, Long publicacionId);
    long countByIdPublicacionId(Long publicacionId);
}
