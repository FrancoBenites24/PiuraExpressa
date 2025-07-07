package com.piuraexpressa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicacionTagDTO {

    @NotNull(message = "El ID de la publicación es obligatorio")
    private Long publicacionId;

    @NotNull(message = "El ID del tag es obligatorio")
    private Long tagId;
}
