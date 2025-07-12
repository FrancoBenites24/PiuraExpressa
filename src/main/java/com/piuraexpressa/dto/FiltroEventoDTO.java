package com.piuraexpressa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltroEventoDTO {
    
    @Size(max = 100, message = "El texto de búsqueda no debe exceder 100 caracteres")
    private String texto;

    @Size(max = 50, message = "El nombre de la provincia no debe exceder 50 caracteres")
    private String provincia;

    @Size(max = 20, message = "Filtro de fecha inválido")
    private String fecha;

    @Size(max = 20, message = "Filtro de precio inválido")
    private String precio;

    @Min(value = 0, message = "La página debe ser mayor o igual a 0")
    private int pagina = 0;

    @Min(value = 1, message = "El tamaño debe ser al menos 1")
    private int tamanio = 10;
}
