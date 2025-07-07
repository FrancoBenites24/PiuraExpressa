package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPermisoRecursoDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El nombre del recurso es obligatorio")
    private String recurso;

    @NotBlank(message = "La acción es obligatoria")
    private String accion;

    private boolean activo = true;
}
