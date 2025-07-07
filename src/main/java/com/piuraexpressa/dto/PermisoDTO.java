package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PermisoDTO {

    private Long id;
    
    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El recurso es obligatorio")
    @Size(max = 100, message = "El recurso no debe exceder los 100 caracteres")
    private String recurso;

    @NotBlank(message = "La acción es obligatoria")
    @Size(max = 50, message = "La acción no debe exceder los 50 caracteres")
    private String accion;

    private boolean activo = true;

    private LocalDate fechaBaja;
    private String motivoBaja;
}
