package com.piuraexpressa.servicio;

public interface ReportePublicacionServicio {
    void reportarPublicacion(Long publicacionId, Long usuarioId);

    long contarReportesPorPublicacion(Long publicacionId);
}
