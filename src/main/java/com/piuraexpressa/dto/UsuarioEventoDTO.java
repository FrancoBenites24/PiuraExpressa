package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEventoDTO {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long eventoId;

    private LocalDateTime fechaRegistro;

    private boolean confirmado;

    private String observaciones;
}
