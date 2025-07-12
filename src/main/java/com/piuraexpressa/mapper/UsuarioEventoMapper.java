package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioEventoDTO;
import com.piuraexpressa.model.dominio.UsuarioEvento;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioEventoMapper {

    @Mapping(target = "usuarioId", source = "id.usuarioId")
    @Mapping(target = "eventoId", source = "id.eventoId")
    UsuarioEventoDTO toDto(UsuarioEvento usuarioEvento);

    List<UsuarioEventoDTO> toDtoLista(List<UsuarioEvento> lista);

    @Mapping(target = "id", expression = "java(new UsuarioEventoId(dto.getUsuarioId(), dto.getEventoId()))")
    UsuarioEvento toEntidad(UsuarioEventoDTO dto);

    List<UsuarioEvento> toEntidadLista(List<UsuarioEventoDTO> lista);
}
