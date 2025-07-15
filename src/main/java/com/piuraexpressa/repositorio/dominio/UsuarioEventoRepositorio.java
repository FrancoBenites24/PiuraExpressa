package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.UsuarioEvento;
import com.piuraexpressa.model.dominio.UsuarioEventoId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioEventoRepositorio extends JpaRepository<UsuarioEvento, UsuarioEventoId> {

    boolean existsByIdUsuarioIdAndIdEventoId(Long usuarioId, Long eventoId);

    int countByIdEventoId(Long eventoId);

    List<UsuarioEvento> findByIdUsuarioId(Long usuarioId);

    int countByIdUsuarioId(Long usuarioId);

    Page<UsuarioEvento> findByIdUsuarioId(Long usuarioId, Pageable pageable);
}
