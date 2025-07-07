package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.TipoReaccionDTO;
import com.piuraexpressa.model.dominio.TipoReaccion;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TipoReaccionMapper {

    TipoReaccionDTO toDto(TipoReaccion entidad);

    TipoReaccion toEntidad(TipoReaccionDTO dto);

    List<TipoReaccionDTO> toDtoLista(List<TipoReaccion> entidades);

    List<TipoReaccion> toEntidadLista(List<TipoReaccionDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(TipoReaccionDTO dto, @MappingTarget TipoReaccion entidad);
}
