package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Comentario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {
    long countByPublicacionId(Long publicacionId);

    List<Comentario> findByPublicacionId(Long publicacionId);

    List<Comentario> findByUsuarioId(Long usuarioId);

    long countByUsuarioId(Long usuarioId);

    Page<Comentario> findByUsuarioId(Long usuarioId, Pageable pageable);
}
