package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResenaRepositorio extends JpaRepository<Resena, Long> {

    boolean existsByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);

    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.evento.id = :eventoId AND r.activa = true")
    Double obtenerPromedioPorEventoId(@Param("eventoId") Long eventoId);

}
