package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.model.dominio.ReportePublicacion;
import com.piuraexpressa.repositorio.dominio.ReportePublicacionRepositorio;
import com.piuraexpressa.servicio.ReportePublicacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportePublicacionServicioImpl implements ReportePublicacionServicio {

    private final ReportePublicacionRepositorio reportePublicacionRepositorio;

    @Override
    public void reportarPublicacion(Long publicacionId, Long usuarioId) {
        boolean yaReportado = reportePublicacionRepositorio.findByPublicacionIdAndUsuarioId(publicacionId, usuarioId).isPresent();
        if (yaReportado) {
            throw new RuntimeException("La publicación ya ha sido reportada por este usuario");
        }

        ReportePublicacion reporte = new ReportePublicacion();
        reporte.setPublicacionId(publicacionId);
        reporte.setUsuarioId(usuarioId);
        reporte.setFechaReporte(LocalDateTime.now());

        reportePublicacionRepositorio.save(reporte);
    }
}
