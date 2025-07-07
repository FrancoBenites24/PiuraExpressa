package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.PermisoDTO;
import com.piuraexpressa.model.seguridad.Permiso;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermisoMapper {

    PermisoDTO toDto(Permiso entidad);

    Permiso toEntidad(PermisoDTO dto);

    List<PermisoDTO> toDtoLista(List<Permiso> entidades);

    List<Permiso> toEntidadLista(List<PermisoDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(PermisoDTO dto, @MappingTarget Permiso entidad);
}
