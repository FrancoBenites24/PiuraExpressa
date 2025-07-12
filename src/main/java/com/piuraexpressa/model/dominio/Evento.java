package com.piuraexpressa.model.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.geolatte.geom.Point;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Size(max = 255)
    @Column(name = "ubicacion", length = 255)
    private String ubicacion;

    @Min(0)
    @Column(name = "capacidad")
    private Integer capacidad;

    @DecimalMin("0.0")
    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "imagen", columnDefinition = "text")
    private String imagen;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "motivo_baja", columnDefinition = "text")
    private String motivoBaja;

    @Column(name = "coordenadas", columnDefinition = "geography(Point,4326)")
    private Point coordenadas;
}
