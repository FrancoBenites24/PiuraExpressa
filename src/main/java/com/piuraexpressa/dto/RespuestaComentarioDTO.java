package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RespuestaComentarioDTO {

    private Long id;

    @NotNull(message = "El ID del comentario es obligatorio")
    private Long comentarioId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "La respuesta no puede estar vacía")
    @Size(max = 1000, message = "La respuesta no debe exceder los 1000 caracteres")
    private String contenido;

    private boolean activo = true;
    private LocalDateTime fechaRespuesta;
}
