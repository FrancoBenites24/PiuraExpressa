package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.RolDTO;
import com.piuraexpressa.model.seguridad.Permiso;
import com.piuraexpressa.model.seguridad.Rol;

import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolMapper {

    @Mapping(target = "permisos", expression = "java(mapPermisosToIds(rol.getPermisos()))")
    RolDTO toDto(Rol rol);

    @Mapping(target = "permisos", ignore = true) // Puedes asignarlos en el servicio
    Rol toEntidad(RolDTO dto);

    List<RolDTO> toDtoLista(List<Rol> roles);

    // Método auxiliar para convertir Set<Permiso> a List<Long>
    default List<Long> mapPermisosToIds(Set<Permiso> permisos) {
        if (permisos == null) return null;
        return permisos.stream()
                .map(Permiso::getId)
                .collect(Collectors.toList());
    }
}
