package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioLikePublicacionDTO {

    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La publicación es obligatoria")
    private Long publicacionId;

    @NotNull(message = "El tipo de reacción es obligatorio")
    private Long tipoReaccionId;

    private LocalDateTime fechaRegistro;

    private boolean activo = true;
}
