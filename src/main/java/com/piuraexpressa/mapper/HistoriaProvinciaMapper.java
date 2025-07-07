package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.HistoriaProvinciaDTO;
import com.piuraexpressa.model.dominio.HistoriaProvincia;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoriaProvinciaMapper {

    HistoriaProvinciaDTO toDto(HistoriaProvincia entidad);

    HistoriaProvincia toEntidad(HistoriaProvinciaDTO dto);

    List<HistoriaProvinciaDTO> toDtoLista(List<HistoriaProvincia> entidades);

    List<HistoriaProvincia> toEntidadLista(List<HistoriaProvinciaDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(HistoriaProvinciaDTO dto, @MappingTarget HistoriaProvincia entidad);
}
