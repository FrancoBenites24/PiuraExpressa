package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioPermisoRecursoDTO;
import com.piuraexpressa.model.seguridad.UsuarioPermisoRecurso;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioPermisoRecursoMapper {

    UsuarioPermisoRecursoDTO toDto(UsuarioPermisoRecurso entidad);

    UsuarioPermisoRecurso toEntidad(UsuarioPermisoRecursoDTO dto);

    List<UsuarioPermisoRecursoDTO> toDtoLista(List<UsuarioPermisoRecurso> entidades);

    List<UsuarioPermisoRecurso> toEntidadLista(List<UsuarioPermisoRecursoDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(UsuarioPermisoRecursoDTO dto, @MappingTarget UsuarioPermisoRecurso entidad);
}
