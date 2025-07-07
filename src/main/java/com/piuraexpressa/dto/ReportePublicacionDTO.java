package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportePublicacionDTO {

    private Long id;

    @NotNull(message = "El ID de la publicación es obligatorio")
    private Long publicacionId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El motivo del reporte es obligatorio")
    private String motivo;

    private boolean activo = true;
    private LocalDateTime fechaReporte;
}
