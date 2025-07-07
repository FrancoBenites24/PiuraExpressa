package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoReaccionDTO {

    private Long id;

    @NotBlank(message = "El nombre de la reacción es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres")
    private String nombre;

    private boolean activo = true;
}
