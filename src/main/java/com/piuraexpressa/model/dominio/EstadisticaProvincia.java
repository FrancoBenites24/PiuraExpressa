package com.piuraexpressa.model.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "estadisticas_provincia", 
       uniqueConstraints = {@UniqueConstraint(columnNames = {"provincia_id", "ano"})})
public class EstadisticaProvincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;

    @Min(1900)
    @Column(nullable = false)
    private Integer ano;

    @Min(1)
    @Column(name = "poblacion_total")
    private Integer poblacionTotal;

    @DecimalMin("0.0")
    @Column(name = "densidad_poblacional")
    private BigDecimal densidadPoblacional;

    @DecimalMin("0.0") @DecimalMax("100.0")
    @Column(name = "indice_alfabetizacion")
    private BigDecimal indiceAlfabetizacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
