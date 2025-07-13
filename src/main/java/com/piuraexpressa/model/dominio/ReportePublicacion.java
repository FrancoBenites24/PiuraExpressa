package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_publicacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"publicacion_id", "usuario_id"})
})
public class ReportePublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publicacion_id", nullable = false)
    private Long publicacionId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    public ReportePublicacion() {
    }

    public ReportePublicacion(Long publicacionId, Long usuarioId, LocalDateTime fechaReporte) {
        this.publicacionId = publicacionId;
        this.usuarioId = usuarioId;
        this.fechaReporte = fechaReporte;
    }

    public Long getId() {
        return id;
    }

    public Long getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Long publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }
}
