package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaProvinciaDTO {

    private Long id;

    @NotNull
    private Long provinciaId;

    @NotNull
    @Min(1900)
    private Integer anoActualizacion;

    @Min(1)
    private Integer poblacionTotal;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal densidadPoblacional;

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    private BigDecimal indiceAlfabetizacion;

    private LocalDateTime fechaCreacion;
}
