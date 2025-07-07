package com.piuraexpressa.model.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "noticias", uniqueConstraints = {
    @UniqueConstraint(columnNames = "slug")
})
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String titulo;

    @Size(max = 300)
    @Column(length = 300)
    private String subtitulo;

    @Lob @NotBlank
    @Column(nullable = false)
    private String contenido;

    @Size(max = 500)
    @Column(length = 500)
    private String resumen;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @NotNull
    @Column(name = "autor_id", nullable = false)
    private Integer autorId;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column
    private Boolean destacada = false;

    @Column
    private Boolean activa = true;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Size(max = 255)
    @Column(length = 255)
    private String slug;

    @Size(max = 160)
    @Column(name = "meta_descripcion", length = 160)
    private String metaDescripcion;

    @Column
    private Integer vistas = 0;
}
