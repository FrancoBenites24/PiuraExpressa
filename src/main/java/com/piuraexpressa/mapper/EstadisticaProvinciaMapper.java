package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.model.dominio.EstadisticaProvincia;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstadisticaProvinciaMapper {

    @Mapping(source = "anoActualizacion", target = "ano")
    EstadisticaProvincia toEntidad(EstadisticaProvinciaDTO dto);

    EstadisticaProvinciaDTO toDto(EstadisticaProvincia entidad);

    List<EstadisticaProvinciaDTO> toDtoLista(List<EstadisticaProvincia> entidades);

    List<EstadisticaProvincia> toEntidadLista(List<EstadisticaProvinciaDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "anoActualizacion", target = "ano")
    void actualizarEntidadDesdeDto(EstadisticaProvinciaDTO dto, @MappingTarget EstadisticaProvincia entidad);
}

