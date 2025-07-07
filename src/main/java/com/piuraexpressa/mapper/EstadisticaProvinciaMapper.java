package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.model.dominio.EstadisticaProvincia;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstadisticaProvinciaMapper {   

    EstadisticaProvinciaDTO toDto(EstadisticaProvincia entidad);

    EstadisticaProvincia toEntidad(EstadisticaProvinciaDTO dto);

    List<EstadisticaProvinciaDTO> toDtoLista(List<EstadisticaProvincia> entidades);

    List<EstadisticaProvincia> toEntidadLista(List<EstadisticaProvinciaDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(EstadisticaProvinciaDTO dto, @MappingTarget EstadisticaProvincia entidad);
}
