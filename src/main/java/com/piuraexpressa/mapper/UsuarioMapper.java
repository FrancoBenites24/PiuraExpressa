package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.model.seguridad.Usuario;

import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(source = "nombres", target = "nombre")
    @Mapping(source = "apellidos", target = "apellido")
    @Mapping(source = "provincia", target = "provincia")
    @Mapping(target = "provinciaId", ignore = true)
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(source = "nombre", target = "nombres")
    @Mapping(source = "apellido", target = "apellidos")
    @Mapping(source = "provincia", target = "provincia")
    Usuario toEntidad(UsuarioDTO dto);

    List<UsuarioDTO> toDTOLista(List<Usuario> usuarios);
    List<Usuario> toEntidadLista(List<UsuarioDTO> usuariosDto);
}

