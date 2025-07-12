package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.repositorio.dominio.ComentarioRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.servicio.ComentarioServicio;
import com.piuraexpressa.dto.ComentarioDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.ComentarioMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.servicio.PublicacionServicio;

import com.piuraexpressa.model.dominio.Comentario;

import java.time.LocalDateTime;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ComentarioServicioImpl implements ComentarioServicio {

    private final ComentarioRepositorio comentarioRepositorio;
    private final ComentarioMapper comentarioMapper;
    private final UsuarioServicio usuarioServicio;
    private final PublicacionRepositorio publicacionRepositorio;

    private String calcularTiempoTranscurrido(LocalDateTime fecha) {
        if (fecha == null) return "";
        Duration duracion = Duration.between(fecha, LocalDateTime.now());
        long dias = duracion.toDays();
        if (dias > 0) return dias + " días";
        long horas = duracion.toHours();
        if (horas > 0) return horas + " horas";
        long minutos = duracion.toMinutes();
        if (minutos > 0) return minutos + " minutos";
        return "Justo ahora";
    }

    @Override
    public long contarComentariosPorPublicacion(Long publicacionId) {
        return comentarioRepositorio.countByPublicacionId(publicacionId);
    }

    @Override
    public List<ComentarioDTO> listarComentariosPorPublicacion(Long publicacionId) {
        List<ComentarioDTO> dtos = comentarioRepositorio.findByPublicacionId(publicacionId)
                .stream()
                .map(comentarioMapper::toDto)
                .collect(Collectors.toList());

        for (ComentarioDTO dto : dtos) {
            // Obtener nombre de usuario por usuarioId
            usuarioServicio.buscarPorId(dto.getUsuarioId()).ifPresent(usuario -> {
                dto.setNombreUsuario(usuario.getNombre() + " " + usuario.getApellido());
            });
            // Calcular tiempo transcurrido
            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(dto.getFechaRegistro()));
        }
        return dtos;
    }

    @Override
    public ComentarioDTO crearComentario(Long publicacionId, ComentarioDTO dto, String username) {
        // Buscar usuario por username y obtener su ID
        UsuarioDTO usuarioDTO = usuarioServicio.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Long usuarioId = usuarioDTO.getId();

        // Mapear DTO a entidad
        Comentario comentario = comentarioMapper.toEntidad(dto);

        // Asociar comentario con usuarioId y publicación
        comentario.setUsuarioId(usuarioId);
        comentario.setPublicacion(publicacionRepositorio.getReferenceById(publicacionId));

        comentario.setFechaCreacion(LocalDateTime.now());

        // Guardar comentario
        Comentario saved = comentarioRepositorio.save(comentario);

        // Mapear entidad guardada a DTO y devolver
        ComentarioDTO savedDto = comentarioMapper.toDto(saved);

        // Setear nombreUsuario y tiempoTranscurrido en DTO guardado
        savedDto.setNombreUsuario(usuarioDTO.getNombre() + " " + usuarioDTO.getApellido());
        savedDto.setTiempoTranscurrido(calcularTiempoTranscurrido(saved.getFechaCreacion()));

        return savedDto;
    }
}
