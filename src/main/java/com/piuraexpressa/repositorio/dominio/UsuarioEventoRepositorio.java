package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.UsuarioEvento;
import com.piuraexpressa.model.dominio.UsuarioEventoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioEventoRepositorio extends JpaRepository<UsuarioEvento, UsuarioEventoId> {

    boolean existsByIdUsuarioIdAndIdEventoId(Long usuarioId, Long eventoId);

    int countByIdEventoId(Long eventoId);
}
