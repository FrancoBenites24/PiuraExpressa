package com.piuraexpressa.model.seguridad;

import jakarta.persistence.*;

@Entity
@Table(name = "ruta_permiso", uniqueConstraints = @UniqueConstraint(columnNames = "ruta_pattern"))
public class RutaPermiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ruta_pattern", nullable = false, length = 200)
    private String rutaPattern;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permiso permiso;
}