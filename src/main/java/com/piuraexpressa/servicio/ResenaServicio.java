package com.piuraexpressa.servicio;

import java.util.List;

import com.piuraexpressa.dto.ResenaDTO;

public interface ResenaServicio {
    ResenaDTO guardarResena(ResenaDTO dto);

    List<ResenaDTO> obtenerResenasPorEvento(Long eventoId);

}
