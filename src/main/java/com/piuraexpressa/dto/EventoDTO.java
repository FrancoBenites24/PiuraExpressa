package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventoDTO {

    private Long id;

    @NotBlank(message = "El título del evento es obligatorio")
    @Size(max = 150, message = "El título no debe exceder los 150 caracteres")
    private String titulo;

    @Size(max = 5000, message = "La descripción no debe ser excesivamente larga")
    private String descripcion;

    @FutureOrPresent(message = "La fecha del evento debe ser actual o futura")
    private LocalDate fecha;

    @NotNull(message = "Se debe asociar una provincia")
    private Long provinciaId;

    private boolean activo = true;

    private String motivoBaja;

    private String fechaBaja;
}
