package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.RolPermisoIdDTO;
import com.piuraexpressa.model.seguridad.RolPermisoId;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolPermisoIdMapper {

    RolPermisoIdDTO toDto(RolPermisoId entidad);

    RolPermisoId toEntidad(RolPermisoIdDTO dto);

    List<RolPermisoIdDTO> toDtoLista(List<RolPermisoId> entidades);

    List<RolPermisoId> toEntidadLista(List<RolPermisoIdDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(RolPermisoIdDTO dto, @MappingTarget RolPermisoId entidad);
}
