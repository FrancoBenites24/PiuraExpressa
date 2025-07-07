package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioLikePublicacionDTO;
import com.piuraexpressa.model.dominio.UsuarioLikePublicacion;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioLikePublicacionMapper {

    @Mapping(source = "id.usuarioId", target = "usuarioId")
    @Mapping(source = "id.publicacionId", target = "publicacionId")
    UsuarioLikePublicacionDTO toDTO(UsuarioLikePublicacion entity);

    @InheritInverseConfiguration
    UsuarioLikePublicacion toEntity(UsuarioLikePublicacionDTO dto);
}
