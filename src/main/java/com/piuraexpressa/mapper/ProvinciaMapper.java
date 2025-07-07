package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.dto.PuntoInteresDTO;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.PuntoInteres;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { PuntoInteresMapper.class })
public interface ProvinciaMapper {

    // ======== ENTITY → DTO ========
    @Mapping(source = "historia", target = "historia")
    @Mapping(source = "latitud", target = "latitud")
    @Mapping(source = "longitud", target = "longitud")
    @Mapping(target = "puntosInteres", expression = "java(toDtoSetFromSet(entidad.getPuntosInteres()))")
    ProvinciaDTO toDto(Provincia entidad);

    // ======== DTO → ENTITY ========
    @InheritInverseConfiguration
    @Mapping(target = "coordenadas", expression = "java(dtoToPoint(dto.getLongitud(), dto.getLatitud()))")
    @Mapping(target = "puntosInteres", expression = "java(toEntitySetFromList(dto.getPuntosInteres()))")
    Provincia toEntidad(ProvinciaDTO dto);

    // ======== LISTAS ========
    List<ProvinciaDTO> toDtoLista(List<Provincia> entidades);

    List<Provincia> toEntidadLista(List<ProvinciaDTO> dtos);

    // ======== PUNTOS INTERES ========
    default Set<PuntoInteresDTO> toDtoSetFromSet(Set<PuntoInteres> set) {
        return (set != null) ? set.stream().map(this::mapPunto).collect(Collectors.toSet()) : Set.of();
    }

    default Set<PuntoInteres> toEntitySetFromList(Collection<PuntoInteresDTO> list) {
        return (list != null) ? list.stream().map(this::mapDto).collect(Collectors.toSet()) : Set.of();
    }

    PuntoInteresDTO mapPunto(PuntoInteres p);

    PuntoInteres mapDto(PuntoInteresDTO dto);

    // ======== COORDENADAS ========
    default Point<G2D> dtoToPoint(Double longitud, Double latitud) {
        if (longitud == null || latitud == null)
            return null;
        return DSL.point(CoordinateReferenceSystems.WGS84, new G2D(longitud, latitud));
    }
}
