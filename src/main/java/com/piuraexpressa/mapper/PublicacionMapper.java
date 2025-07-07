package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PublicacionMapper {

    @Autowired
    protected UsuarioRepositorio usuarioRepositorio;

    public abstract PublicacionDTO toDto(Publicacion entidad);

    public abstract Publicacion toEntidad(PublicacionDTO dto);

    public abstract List<PublicacionDTO> toDtoLista(List<Publicacion> entidades);

    public abstract List<Publicacion> toEntidadLista(List<PublicacionDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void actualizarEntidadDesdeDto(PublicacionDTO dto, @MappingTarget Publicacion entidad);

    // ------ Agrega este método para mapear los datos del usuario
    @AfterMapping
    protected void completarDatosUsuario(Publicacion entidad, @MappingTarget PublicacionDTO dto) {
        if (entidad.getAutor() != null) {
            usuarioRepositorio.findById(entidad.getAutor()).ifPresent(usuario -> {
                dto.setUsuarioId(usuario.getId());
                dto.setNombreUsuario(usuario.getNombres() + " " + usuario.getApellidos());
                dto.setAvatarUsuario("/img/default-profile.png");
            });
        }
    }
}
