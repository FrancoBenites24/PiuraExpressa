package com.piuraexpressa.model.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "archivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @NotBlank
    @Column(nullable = false)
    private String url;

    @Column(length = 50)
    private String tipo;

    @Column(name = "referencia_tipo", length = 50)
    private String referenciaTipo;

    private Long referenciaId;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Builder.Default
    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida = LocalDateTime.now();
}
