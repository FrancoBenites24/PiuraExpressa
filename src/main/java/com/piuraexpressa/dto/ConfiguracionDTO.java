package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracionDTO {

    private Long id;

    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 100, message = "La clave no debe exceder los 100 caracteres")
    private String clave;

    @Size(max = 1000, message = "El valor no debe exceder los 1000 caracteres")
    private String valor;

    @Size(max = 500, message = "La descripción no debe exceder los 500 caracteres")
    private String descripcion;

    private boolean activo = true;

    private String motivoBaja;

    private String fechaBaja;
}
