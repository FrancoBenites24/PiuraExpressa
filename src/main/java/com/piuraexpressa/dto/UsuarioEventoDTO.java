package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioEventoDTO {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El evento es obligatorio")
    private Long eventoId;

    private boolean asistira;
    private LocalDateTime fechaRegistro;
    private boolean activo = true;
}
