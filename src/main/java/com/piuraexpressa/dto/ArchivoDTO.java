package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivoDTO {

    private Long id;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 255, message = "El nombre del archivo no debe exceder los 255 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo MIME es obligatorio")
    @Size(max = 100, message = "El tipo MIME no debe exceder los 100 caracteres")
    private String tipoMime;

    @NotBlank(message = "La URL del archivo es obligatoria")
    @Size(max = 500, message = "La URL no debe exceder los 500 caracteres")
    private String url;

    @NotNull(message = "El tamaño del archivo es obligatorio")
    @Min(value = 0, message = "El tamaño debe ser un valor positivo")
    private Long tamanioBytes;

    private boolean activo = true;

    private String motivoBaja;

    private String fechaBaja;
}
