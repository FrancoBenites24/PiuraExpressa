package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.ComentarioDTO;
import com.piuraexpressa.model.dominio.Comentario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {
    ComentarioDTO toDto(Comentario entidad);
    Comentario toEntidad(ComentarioDTO dto);
    List<ComentarioDTO> toDtoLista(List<Comentario> entidades);
    List<Comentario> toEntidadLista(List<ComentarioDTO> dtos);
}
