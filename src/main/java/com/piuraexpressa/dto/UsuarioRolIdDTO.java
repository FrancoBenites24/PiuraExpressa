package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class UsuarioRolIdDTO implements Serializable {

    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(message = "El ID de rol no puede ser nulo")
    private Long rolId;
}
