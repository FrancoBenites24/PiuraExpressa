package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolPermisoDTO {

    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;

    @NotNull(message = "El ID del permiso es obligatorio")
    private Long permisoId;

    private boolean activo = true;
}
