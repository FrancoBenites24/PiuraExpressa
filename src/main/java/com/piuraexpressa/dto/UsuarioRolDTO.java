package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsuarioRolDTO {

    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El rol es obligatorio")
    private Long rolId;

    private boolean activo = true;

    private LocalDate fechaBaja;
    private String motivoBaja;
}
