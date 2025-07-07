package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticiaDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no debe exceder los 255 caracteres")
    private String titulo;

    @Size(max = 500, message = "El resumen no debe exceder los 500 caracteres")
    private String resumen;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    @NotNull(message = "Debe especificarse si la noticia está activa o no")
    private Boolean activo;

    private LocalDateTime fechaPublicacion;

    @NotNull(message = "La categoría de la noticia es obligatoria")
    private Long categoriaId;

    private String imagenPrincipal;

    private LocalDateTime fechaBaja;

    @Size(max = 255, message = "El motivo de baja no debe exceder los 255 caracteres")
    private String motivoBaja;
}
