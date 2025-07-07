package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_publicacion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReportePublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    @Builder.Default
    @Column(nullable = false)
    private boolean atendido = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
}
