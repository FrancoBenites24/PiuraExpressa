package com.piuraexpressa.util;

/*import com.piuraexpressa.dto.ComentarioDTO;
import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.model.Comentario;
import com.piuraexpressa.model.Publicacion;
import com.piuraexpressa.model.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;*/

public class MapperUtil {
/* 
    // === PUBLICACIÓN ===

    public static PublicacionDTO toDto(Publicacion entidad) {
        if (entidad == null) return null;

        Usuario usuario = entidad.getUsuario();

        return PublicacionDTO.builder()
                .id(entidad.getId())
                .usuarioId(usuario != null ? usuario.getId() : null)
                .usuarioNombre(usuario != null ? usuario.getNombreCompleto() : null)
                .usuarioUsername(usuario != null ? usuario.getUsername() : null)
                .titulo(entidad.getTitulo())
                .contenido(entidad.getContenido())
                .imagen(entidad.getImagen())
                .activa(entidad.isActiva())
                .fechaCreacion(entidad.getFechaCreacion())
                .fechaActualizacion(entidad.getFechaActualizacion())
                .contenidoResumen(entidad.getContenidoResumen())
                .totalComentarios(entidad.getTotalComentarios())
                .build();
    }

    public static Publicacion toEntity(PublicacionDTO dto) {
        if (dto == null) return null;

        Publicacion entidad = new Publicacion();
        entidad.setId(dto.getId());

        // Solo se establece el ID del usuario, no se carga completo.
        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            entidad.setUsuario(usuario);
        }

        entidad.setTitulo(dto.getTitulo());
        entidad.setContenido(dto.getContenido());
        entidad.setImagen(dto.getImagen());
        entidad.setActiva(dto.isActiva());
        entidad.setFechaCreacion(dto.getFechaCreacion());

        return entidad;
    }

    // === COMENTARIO ===

    public static ComentarioDTO toDto(Comentario comentario) {
        if (comentario == null) return null;

        Usuario usuario = comentario.getUsuario();
        Publicacion publicacion = comentario.getPublicacion();

        return ComentarioDTO.builder()
                .id(comentario.getId())
                .contenido(comentario.getContenido())
                .usuarioId(usuario != null ? usuario.getId() : null)
                .usuarioNombre(usuario != null ? usuario.getNombreCompleto() : null)
                .usuarioUsername(usuario != null ? usuario.getUsername() : null)
                .publicacionId(publicacion != null ? publicacion.getId() : null)
                .publicacionTitulo(publicacion != null ? publicacion.getTitulo() : null)
                .build();
    }

    public static Comentario toEntity(ComentarioDTO dto) {
        if (dto == null) return null;

        Comentario comentario = new Comentario();
        comentario.setId(dto.getId());
        comentario.setContenido(dto.getContenido());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            comentario.setUsuario(usuario);
        }

        if (dto.getPublicacionId() != null) {
            Publicacion publicacion = new Publicacion();
            publicacion.setId(dto.getPublicacionId());
            comentario.setPublicacion(publicacion);
        }

        return comentario;
    }

    // === CONVERSIÓN DE LISTAS / SETS ===

    public static List<PublicacionDTO> toPublicacionDtoList(List<Publicacion> publicaciones) {
        return publicaciones == null ? List.of() :
                publicaciones.stream().map(MapperUtil::toDto).collect(Collectors.toList());
    }

    public static List<ComentarioDTO> toComentarioDtoList(Set<Comentario> comentarios) {
        return comentarios == null ? List.of() :
                comentarios.stream().map(MapperUtil::toDto).collect(Collectors.toList());
    }

    public static Usuario mapToUsuario(com.piuraexpressa.dto.UsuarioDTO dto) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        // usuario.setNombreCompleto(dto.getNombreCompleto()); // Removed because setNombreCompleto does not exist in Usuario
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        // Add other fields as needed
        return usuario;
    }*/
}
