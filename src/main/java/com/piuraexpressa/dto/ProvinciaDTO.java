package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProvinciaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    @Size(max = 255, message = "El subtítulo no puede tener más de 255 caracteres.")
    private String subtitulo;

    @Size(max = 5000, message = "La descripción es demasiado larga.")
    private String descripcion;

    @NotNull(message = "La latitud es obligatoria.")
    @DecimalMin(value = "-90.0", message = "Latitud mínima: -90.")
    @DecimalMax(value = "90.0", message = "Latitud máxima: 90.")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria.")
    @DecimalMin(value = "-180.0", message = "Longitud mínima: -180.")
    @DecimalMax(value = "180.0", message = "Longitud máxima: 180.")
    private Double longitud;

    @NotNull(message = "La altitud es obligatoria.")
    @Min(value = 0, message = "La altitud no puede ser negativa.")
    private Integer altitudPromedio;

    @Size(max = 1000, message = "La descripción climática es demasiado larga.")
    private String climaDescripcion;

    @Size(max = 1000, message = "La mejor época para visitar es demasiado larga.")
    private String mejorEpocaVisita;

    @Size(max = 255, message = "La URL de la imagen es demasiado larga.")
    private String imagenPrincipal;

    private boolean activo = true;

    private String motivoBaja;

    private String fechaBaja; // Puedes cambiar esto a LocalDateTime si lo manejas así en tu entidad
    
    private List<HistoriaProvinciaDTO> historia;

    private Set<PuntoInteresDTO> puntosInteres;

    private List<EstadisticaProvinciaDTO> estadisticas;
}
