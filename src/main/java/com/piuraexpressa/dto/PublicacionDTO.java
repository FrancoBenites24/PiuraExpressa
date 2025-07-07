package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PublicacionDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no debe exceder los 255 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    private String imagenPrincipal;

    @NotNull(message = "Debe indicarse si está activa")
    private Boolean activo;

    private LocalDateTime fechaPublicacion;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private LocalDateTime fechaBaja;

    @Size(max = 255, message = "El motivo de baja no debe exceder los 255 caracteres")
    private String motivoBaja;

    private Long provinciaId;
    private String nombreUsuario;
    private String avatarUsuario;
}
