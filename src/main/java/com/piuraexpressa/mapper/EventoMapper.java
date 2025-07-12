package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    // Mapear entidad a DTO con contexto (repositorio)
    @Mapping(target = "provincia", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "porcentajeOcupado", ignore = true)
    @Mapping(target = "promedioCalificacion", ignore = true)
    @Mapping(target = "cantidadParticipantes", ignore = true)
    @Mapping(target = "cantidadResenas", ignore = true)
    @Mapping(target = "imagenUrl", source = "imagen")
    EventoDTO toDto(Evento evento, @Context ResenaRepositorio resenaRepositorio);

    List<EventoDTO> toDtoLista(List<Evento> eventos, @Context ResenaRepositorio resenaRepositorio);

    // Mapear DTO a entidad
    @InheritInverseConfiguration
    @Mapping(target = "provincia", ignore = true)
    @Mapping(source = "usuarioId", target = "usuarioId")
    @Mapping(target = "coordenadas", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "motivoBaja", ignore = true)
    Evento toEntidad(EventoDTO dto);

    List<Evento> toEntidadLista(List<EventoDTO> dtos);

    @AfterMapping
    default void enrichDto(Evento evento, @MappingTarget EventoDTO dto, @Context ResenaRepositorio resenaRepositorio) {
        if (evento.getProvincia() != null) {
            dto.setProvincia(evento.getProvincia().getNombre());
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (evento.getFechaInicio() != null && evento.getFechaFin() != null) {
            if (ahora.isBefore(evento.getFechaInicio())) {
                dto.setEstado("PROXIMO");
                dto.setFinalizado(false);
            } else if (ahora.isAfter(evento.getFechaFin())) {
                dto.setEstado("FINALIZADO");
                dto.setFinalizado(true);
            } else {
                dto.setEstado("EN_CURSO");
                dto.setFinalizado(false);
            }
        }

        // Calcular promedio de calificación desde la base
        Double promedio = resenaRepositorio.obtenerPromedioPorEventoId(evento.getId());
        dto.setPromedioCalificacion(promedio != null ? promedio : 0.0);

        // dto.setPorcentajeOcupado(null);
        // dto.setCantidadParticipantes(null);
        // dto.setCantidadResenas(null);
        // dto.setParticipantesActuales(null);

        // dto.setPuedeParticipar(false);
        // dto.setYaParticipa(false);
        // dto.setYaResenado(false);
    }
}
