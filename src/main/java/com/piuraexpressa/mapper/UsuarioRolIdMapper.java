package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioRolIdDTO;
import com.piuraexpressa.model.seguridad.UsuarioRolId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioRolIdMapper {

    UsuarioRolIdMapper INSTANCE = Mappers.getMapper(UsuarioRolIdMapper.class);

    UsuarioRolIdDTO toDto(UsuarioRolId entity);

    UsuarioRolId toEntity(UsuarioRolIdDTO dto);

    List<UsuarioRolIdDTO> toDtoList(List<UsuarioRolId> entities);

    List<UsuarioRolId> toEntityList(List<UsuarioRolIdDTO> dtos);
}
