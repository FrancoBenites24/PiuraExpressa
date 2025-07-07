package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EstadisticaProvinciaDTO {

    private Long id;

    @NotNull(message = "La provincia es obligatoria")
    private Long provinciaId;

    @NotNull(message = "El año de actualización es obligatorio")
    private Integer anoActualizacion;

    @Min(value = 0, message = "La población debe ser un número positivo")
    private Long poblacion;

    @DecimalMin(value = "0.0", inclusive = true, message = "El PIB debe ser un número positivo")
    private Double pib;

    @Min(value = 0, message = "La cantidad de hoteles debe ser positiva")
    private Integer hotelesRegistrados;

    @Min(value = 0, message = "La cantidad de restaurantes debe ser positiva")
    private Integer restaurantesRegistrados;

    @Min(value = 0, message = "La cantidad de atractivos turísticos debe ser positiva")
    private Integer atractivosTuristicos;

    @Min(value = 0, message = "La cantidad de festivales anuales debe ser positiva")
    private Integer festivalesAnuales;

    private boolean activo = true;

    private LocalDate fechaBaja;

    @Size(max = 255, message = "El motivo de baja no debe exceder los 255 caracteres")
    private String motivoBaja;
}
