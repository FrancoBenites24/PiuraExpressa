package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.TagDTO;
import com.piuraexpressa.model.dominio.Tag;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    TagDTO toDto(Tag entidad);

    Tag toEntidad(TagDTO dto);

    List<TagDTO> toDtoLista(List<Tag> entidades);

    List<Tag> toEntidadLista(List<TagDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(TagDTO dto, @MappingTarget Tag entidad);
}
