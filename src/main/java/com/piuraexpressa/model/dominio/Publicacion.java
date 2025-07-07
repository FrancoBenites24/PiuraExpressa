package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no debe exceder los 150 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    @Size(max = 10000, message = "El contenido es demasiado extenso")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Builder.Default
    @Column(name = "activa",nullable = false)
    private boolean activo = true;

    @Column(name = "usuario_id", nullable = false)
    private Long autor;

    @Column(nullable = false)
    private String slug;

    //realaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
}
