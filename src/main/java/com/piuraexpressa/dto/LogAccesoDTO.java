package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LogAccesoDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "La IP de acceso es obligatoria")
    @Size(max = 45, message = "La IP no debe exceder los 45 caracteres")
    private String ipAcceso;

    @NotBlank(message = "El tipo de acceso es obligatorio")
    @Size(max = 50, message = "El tipo de acceso no debe exceder los 50 caracteres")
    private String tipoAcceso;

    @NotNull(message = "La fecha de acceso es obligatoria")
    private LocalDateTime fechaAcceso;
}
