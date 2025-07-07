package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RolDTO {

    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no debe exceder los 50 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
    private String descripcion;

    private List<Long> permisos; // Lista de IDs de permisos asociados

    private boolean activo = true;

    private LocalDate fechaBaja;
    private String motivoBaja;
}
