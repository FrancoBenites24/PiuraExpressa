package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ComentarioDTO {

    private Long id;

    @NotNull(message = "El ID de la publicación es obligatorio")
    private Long publicacionId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Size(max = 1000, message = "El comentario no debe exceder los 1000 caracteres")
    private String contenido;

    private LocalDateTime fechaRegistro;

    private boolean activo = true;

    private String motivoBaja;

    private String fechaBaja;

    // Campos adicionales para UI
    private String nombreUsuario;
    private String tiempoTranscurrido;
}
