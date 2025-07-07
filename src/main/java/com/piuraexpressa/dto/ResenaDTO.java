package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResenaDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID del punto de interés es obligatorio")
    private Long puntoInteresId;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private int calificacion;

    @Size(max = 1000, message = "El comentario no debe exceder los 1000 caracteres")
    private String comentario;

    private boolean activo = true;
    private LocalDateTime fechaRegistro;
}
