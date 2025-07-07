package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuntoInteresDTO {

    private Long id;

    @NotNull(message = "La provincia es obligatoria")
    private Long provinciaId;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
    private String descripcion;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @Size(max = 150, message = "La dirección no debe exceder los 150 caracteres")
    private String direccion;

    @Size(max = 30, message = "El teléfono no debe exceder los 30 caracteres")
    private String telefono;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no debe exceder los 100 caracteres")
    private String email;

    @Size(max = 120, message = "El sitio web no debe exceder los 120 caracteres")
    private String sitioWeb;

    @Size(max = 80, message = "El horario no debe exceder los 80 caracteres")
    private String horarioAtencion;

    private Double calificacion;

    @Size(max = 255, message = "La URL de la imagen no debe exceder los 255 caracteres")
    private String imagenUrl;

    private Boolean activo = true;

    private java.time.LocalDateTime fechaCreacion;

    private String categoriaNombre;
}
