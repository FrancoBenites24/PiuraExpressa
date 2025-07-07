package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.ResenaDTO;
import com.piuraexpressa.model.dominio.Resena;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResenaMapper {

    ResenaDTO toDto(Resena entidad);

    Resena toEntidad(ResenaDTO dto);

    List<ResenaDTO> toDtoLista(List<Resena> entidades);

    List<Resena> toEntidadLista(List<ResenaDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(ResenaDTO dto, @MappingTarget Resena entidad);
}
