package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.PuntoInteresDTO;
import com.piuraexpressa.model.dominio.CategoriaPuntoInteres;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.PuntoInteres;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PuntoInteresMapper {

    // ======= ENTITY TO DTO =======

    @Mapping(source = "provincia.id", target = "provinciaId")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(target = "latitud", expression = "java(getLatitud(entity.getCoordenadas()))")
    @Mapping(target = "longitud", expression = "java(getLongitud(entity.getCoordenadas()))")
    PuntoInteresDTO toDto(PuntoInteres entity);

    // ======= DTO TO ENTITY =======
    @Mapping(source = "provinciaId", target = "provincia.id")
    @Mapping(source = "categoriaId", target = "categoria.id")
    @Mapping(target = "coordenadas", expression = "java(toPoint(dto.getLatitud(), dto.getLongitud()))")
    @Mapping(target = "fechaCreacion", ignore = true)
    PuntoInteres toEntidad(PuntoInteresDTO dto);

    // ======= LIST & PAGE =======
    List<PuntoInteresDTO> toDtoList(List<PuntoInteres> list);

    List<PuntoInteres> toEntityList(List<PuntoInteresDTO> list);

    @AfterMapping
    default void setProvinciaCategoria(@MappingTarget PuntoInteres entity, PuntoInteresDTO dto) {
        if (entity.getProvincia() == null && dto.getProvinciaId() != null) {
            Provincia p = new Provincia();
            p.setId(dto.getProvinciaId());
            entity.setProvincia(p);
        }
        if (entity.getCategoria() == null && dto.getCategoriaId() != null) {
            CategoriaPuntoInteres c = new CategoriaPuntoInteres();
            if (dto.getCategoriaId() != null) {
                c.setId(dto.getCategoriaId().intValue());
            }
            entity.setCategoria(c);
        }
    }

    // Page<DTO> mapping (puedes usar map en el service)
    default Page<PuntoInteresDTO> toDtoPage(Page<PuntoInteres> page) {
        return page.map(this::toDto);
    }

    // ======= HELPERS =======

    default Double getLatitud(Point<G2D> punto) {
        return (punto != null) ? punto.getPosition().getLat() : null;
    }

    default Double getLongitud(Point<G2D> punto) {
        return (punto != null) ? punto.getPosition().getLon() : null;
    }

    default Point<G2D> toPoint(Double latitud, Double longitud) {
        if (latitud == null || longitud == null)
            return null;
        return DSL.point(CoordinateReferenceSystems.WGS84, new G2D(longitud, latitud));
    }

}
