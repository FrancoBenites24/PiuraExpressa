package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.PublicacionMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.servicio.ComentarioServicio;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.piuraexpressa.repositorio.dominio.UsuarioLikePublicacionRepositorio;

@Service
@RequiredArgsConstructor

public class PublicacionServicioImpl implements PublicacionServicio {

    private final PublicacionRepositorio publicacionRepositorio;
    private final PublicacionMapper publicacionMapper;
    private final UsuarioServicio usuarioServicio;
    private final ProvinciaRepositorio provinciaRepositorio;
    private final UsuarioLikePublicacionRepositorio usuarioLikePublicacionRepositorio;
    private final ComentarioServicio comentarioServicio;

    private String generarSlug(String titulo) {
        return titulo == null ? "" : titulo
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "") // Solo letras, números y espacios
            .replaceAll("\\s+", "-")        // Espacios por guión
            .replaceAll("-+", "-")          // Un solo guión
            .replaceAll("^-|-$", "");       // Sin guiones al inicio o fin
    }

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
    public PublicacionDTO crearPublicacion(PublicacionDTO dto, String username) {
        // 1. Buscar usuario para obtener su ID (de otra BD)
        UsuarioDTO usuario = usuarioServicio.buscarPorUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 2. Mapear DTO a entidad
        Publicacion publicacion = publicacionMapper.toEntidad(dto);

        // 3. Setear el ID del autor (usuario)
        publicacion.setAutor(usuario.getId());

        // 4. Setear fechas, activo y otros campos
        publicacion.setFechaCreacion(LocalDateTime.now());
        publicacion.setFechaActualizacion(LocalDateTime.now());
        publicacion.setActivo(true);

        // 5. Setear provincia si corresponde
        if (dto.getProvinciaId() != null) {
            Provincia provincia = provinciaRepositorio.findById(dto.getProvinciaId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada"));
            publicacion.setProvincia(provincia);
        } else {
            publicacion.setProvincia(null);
        }

        // 6. Generar slug
        publicacion.setSlug(generarSlug(dto.getTitulo()));

        // 7. Guardar y devolver DTO
        Publicacion saved = publicacionRepositorio.save(publicacion);
        PublicacionDTO resultDto = publicacionMapper.toDto(saved);

        // 8. Setear campos adicionales
        resultDto.setTiempoTranscurrido(calcularTiempoTranscurrido(saved.getFechaCreacion()));
        long totalLikes = usuarioLikePublicacionRepositorio.countByIdPublicacionId(saved.getId());
        resultDto.setTotalLikes(totalLikes);
        long totalComentarios = comentarioServicio.contarComentariosPorPublicacion(saved.getId());
        resultDto.setTotalComentarios(totalComentarios);

        return resultDto;
    }

    @Override
    public Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort != null ? sort : "fechaCreacion"));
        // Este es un ejemplo simple, puedes mejorar el filtro como quieras.
        Page<Publicacion> publicaciones = publicacionRepositorio.findAll(pageable);
        Page<PublicacionDTO> dtos = publicaciones.map(publicacionMapper::toDto);

        // Setear campos adicionales en cada DTO
        dtos.forEach(dto -> {
            // Usar fechaCreacion para calcular tiempo transcurrido, si está disponible
            LocalDateTime fecha = dto.getFechaPublicacion();
            if (fecha == null) {
                // Si fechaPublicacion es null, usar fechaActualizacion o fechaCreacion si se agrega al DTO
                fecha = LocalDateTime.now(); // fallback para evitar null
            }
            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(fecha));
            long totalLikes = usuarioLikePublicacionRepositorio.countByIdPublicacionId(dto.getId());
            dto.setTotalLikes(totalLikes);
            long totalComentarios = comentarioServicio.contarComentariosPorPublicacion(dto.getId());
            dto.setTotalComentarios(totalComentarios);
        });

        return dtos;
    }

    @Override
    public Optional<Publicacion> buscarPorId(Long id) {
        return publicacionRepositorio.findById(id);
    }
}
