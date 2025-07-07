package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDTO {

    private Long id;

    @NotBlank(message = "El nombre del tag es obligatorio")
    @Size(max = 50, message = "El nombre del tag no debe exceder los 50 caracteres")
    private String nombre;

    private boolean activo = true;
}
