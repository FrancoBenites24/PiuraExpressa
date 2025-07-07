package com.piuraexpressa.model.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.geolatte.geom.Point;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
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
    private String titulo;

    private String descripcion;

    @NotNull
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private String ubicacion;

    @Min(0)
    private Integer capacidad;

    @DecimalMin("0.0")
    private BigDecimal precio;

    private Boolean activo = true;

    private String imagen;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    private LocalDateTime fechaBaja;
    private String motivoBaja;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point coordenadas;
}