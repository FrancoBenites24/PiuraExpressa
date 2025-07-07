package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respuesta_comentario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaComentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(nullable = false)
    private String contenido;

    @Column(name = "fecha_creacion",nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion",nullable = false)
    private LocalDateTime fechaActualizacion;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comentario_id", nullable = false)
    private Comentario comentario;
}
