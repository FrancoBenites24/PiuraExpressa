package com.piuraexpressa.model.dominio;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "provincias", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    @Size(max = 255)
    private String subtitulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(name = "imagen_principal", length = 255)
    private String imagenPrincipal;

    @Column(name = "altitud_promedio")
    private Integer altitudPromedio;

    @Size(max = 200)
    @Column(name = "clima_descripcion")
    private String climaDescripcion;

    @Size(max = 100)
    @Column(name = "mejor_epoca_visita")
    private String mejorEpocaVisita;

    @Builder.Default
    @Column
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Builder.Default
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "motivo_baja")
    private String motivoBaja;

    @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
    private Point<G2D> coordenadas;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    // relaciones
    @Builder.Default
    @OneToMany(mappedBy = "provincia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistoriaProvincia> historia = new ArrayList<>();

    @OneToMany(mappedBy = "provincia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PuntoInteres> puntosInteres = new HashSet<>();

}
