package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.PublicacionMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicacionServicioImpl implements PublicacionServicio {

    private final PublicacionRepositorio publicacionRepositorio;
    private final PublicacionMapper publicacionMapper;
    private final UsuarioServicio usuarioServicio;
    private final ProvinciaRepositorio provinciaRepositorio;
    private String generarSlug(String titulo) {
        return titulo == null ? "" : titulo
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "") // Solo letras, números y espacios
            .replaceAll("\\s+", "-")        // Espacios por guión
            .replaceAll("-+", "-")          // Un solo guión
            .replaceAll("^-|-$", "");       // Sin guiones al inicio o fin
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
        return publicacionMapper.toDto(saved);
    }



    @Override
    public Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort != null ? sort : "fechaCreacion"));
        // Este es un ejemplo simple, puedes mejorar el filtro como quieras.
        Page<Publicacion> publicaciones = publicacionRepositorio.findAll(pageable);
        return publicaciones.map(publicacionMapper::toDto);
    }
}
