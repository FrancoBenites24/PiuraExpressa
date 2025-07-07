package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.RolPermisoDTO;
import com.piuraexpressa.model.seguridad.RolPermiso;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolPermisoMapper {

    @Mapping(target = "rolId", source = "id.rolId")
    @Mapping(target = "permisoId", source = "id.permisoId")
    RolPermisoDTO toDto(RolPermiso entidad);

    @Mapping(target = "id", expression = "java(new RolPermisoId(dto.getRolId(), dto.getPermisoId()))")
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "permiso", ignore = true)
    RolPermiso toEntidad(RolPermisoDTO dto);

    List<RolPermisoDTO> toDtoLista(List<RolPermiso> entidades);

    List<RolPermiso> toEntidadLista(List<RolPermisoDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", expression = "java(new RolPermisoId(dto.getRolId(), dto.getPermisoId()))")
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "permiso", ignore = true)
    void actualizarEntidadDesdeDto(RolPermisoDTO dto, @MappingTarget RolPermiso entidad);
}
