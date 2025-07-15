package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.ReportePublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportePublicacionRepositorio extends JpaRepository<ReportePublicacion, Long> {
    Optional<ReportePublicacion> findByPublicacionIdAndUsuarioId(Long publicacionId, Long usuarioId);

    long countByPublicacionId(Long publicacionId);
}
