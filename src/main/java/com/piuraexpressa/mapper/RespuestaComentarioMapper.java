package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.RespuestaComentarioDTO;
import com.piuraexpressa.model.dominio.RespuestaComentario;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RespuestaComentarioMapper {

    RespuestaComentarioDTO toDto(RespuestaComentario entidad);

    RespuestaComentario toEntidad(RespuestaComentarioDTO dto);

    List<RespuestaComentarioDTO> toDtoLista(List<RespuestaComentario> entidades);

    List<RespuestaComentario> toEntidadLista(List<RespuestaComentarioDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(RespuestaComentarioDTO dto, @MappingTarget RespuestaComentario entidad);
}
