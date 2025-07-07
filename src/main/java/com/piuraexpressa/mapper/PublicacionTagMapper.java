package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.PublicacionTagDTO;
import com.piuraexpressa.model.dominio.PublicacionTag;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublicacionTagMapper {

    PublicacionTagDTO toDto(PublicacionTag entidad);

    PublicacionTag toEntidad(PublicacionTagDTO dto);

    List<PublicacionTagDTO> toDtoLista(List<PublicacionTag> entidades);

    List<PublicacionTag> toEntidadLista(List<PublicacionTagDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(PublicacionTagDTO dto, @MappingTarget PublicacionTag entidad);
}
