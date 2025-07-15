package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int calificacion;

    @Size(max = 1000)
    private String contenido;

    @Column(name = "fecha_creacion",nullable = false)
    private LocalDateTime fechaResena;

    @Builder.Default
    @Column(nullable = false)
    private boolean activa = true;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;
}
