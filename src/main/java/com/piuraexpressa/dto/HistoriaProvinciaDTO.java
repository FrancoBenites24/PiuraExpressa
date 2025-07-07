package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HistoriaProvinciaDTO {

    private Long id;

    @NotNull(message = "La provincia es obligatoria")
    private Long provinciaId;

    private Integer ano;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no debe exceder los 100 caracteres")
    private String titulo;

    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
    private String descripcion;

    private Integer ordenCronologico;

    private boolean activo = true;

    private LocalDate fechaBaja;

    @Size(max = 255, message = "El motivo de baja no debe exceder los 255 caracteres")
    private String motivoBaja;
}
