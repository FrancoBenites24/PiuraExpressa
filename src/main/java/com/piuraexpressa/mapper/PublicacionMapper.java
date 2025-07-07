package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.model.dominio.Publicacion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublicacionMapper {

    PublicacionDTO toDto(Publicacion entidad);

    Publicacion toEntidad(PublicacionDTO dto);

    List<PublicacionDTO> toDtoLista(List<Publicacion> entidades);

    List<Publicacion> toEntidadLista(List<PublicacionDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(PublicacionDTO dto, @MappingTarget Publicacion entidad);
}
