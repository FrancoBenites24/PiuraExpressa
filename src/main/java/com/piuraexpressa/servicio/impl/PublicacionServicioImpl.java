package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.PublicacionMapper;
import com.piuraexpressa.model.dominio.Comentario;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.model.dominio.UsuarioLikePublicacion;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.servicio.ComentarioServicio;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.specification.PublicacionSpecification;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
        return titulo == null ? ""
                : titulo
                        .toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", "") // Solo letras, números y espacios
                        .replaceAll("\\s+", "-") // Espacios por guión
                        .replaceAll("-+", "-") // Un solo guión
                        .replaceAll("^-|-$", ""); // Sin guiones al inicio o fin
    }

    private String calcularTiempoTranscurrido(LocalDateTime fecha) {
        if (fecha == null)
            return "";
        Duration duracion = Duration.between(fecha, LocalDateTime.now());
        long dias = duracion.toDays();
        if (dias > 0)
            return dias + " días";
        long horas = duracion.toHours();
        if (horas > 0)
            return horas + " horas";
        long minutos = duracion.toMinutes();
        if (minutos > 0)
            return minutos + " minutos";
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
    public PublicacionDTO actualizarPublicacion(Long id, PublicacionDTO dto, String username) {
        // 1. Buscar la publicación por ID
        Publicacion publicacion = publicacionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // 2. Verificar que el usuario autenticado es el autor
        UsuarioDTO usuario = usuarioServicio.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!publicacion.getAutor().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado para actualizar esta publicación");
        }

        // 3. Actualizar campos permitidos
        publicacion.setTitulo(dto.getTitulo());
        publicacion.setContenido(dto.getContenido());
        publicacion.setFechaActualizacion(LocalDateTime.now());
        if (dto.getActivo() != null) {
            publicacion.setActivo(dto.getActivo());
        } else {
            publicacion.setActivo(false); // or true, depending on default desired behavior
        }

        // 4. Actualizar provincia si corresponde
        if (dto.getProvinciaId() != null) {
            Provincia provincia = provinciaRepositorio.findById(dto.getProvinciaId())
                    .orElseThrow(() -> new RuntimeException("Provincia no encontrada"));
            publicacion.setProvincia(provincia);
        } else {
            publicacion.setProvincia(null);
        }

        // 5. Actualizar slug
        publicacion.setSlug(generarSlug(dto.getTitulo()));

        // 6. Guardar y devolver DTO actualizado
        Publicacion updated = publicacionRepositorio.save(publicacion);
        PublicacionDTO resultDto = publicacionMapper.toDto(updated);

        // 7. Setear campos adicionales
        resultDto.setTiempoTranscurrido(calcularTiempoTranscurrido(updated.getFechaCreacion()));
        long totalLikes = usuarioLikePublicacionRepositorio.countByIdPublicacionId(updated.getId());
        resultDto.setTotalLikes(totalLikes);
        long totalComentarios = comentarioServicio.contarComentariosPorPublicacion(updated.getId());
        resultDto.setTotalComentarios(totalComentarios);

        return resultDto;
    }

    @Override
    public void eliminarPublicacion(Long id, String username) {
        Publicacion publicacion = publicacionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        UsuarioDTO usuario = usuarioServicio.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!publicacion.getAutor().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado para eliminar esta publicación");
        }

        publicacionRepositorio.delete(publicacion);
    }

    @Override
    public Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId, Long idUsuarioAutenticado) {
        // Inicializa Specification con cb.conjunction() (evita el uso de where
        // deprecado)
        Specification<Publicacion> spec = (root, query, cb) -> cb.conjunction();

        if (usuarioId != null) {
            spec = spec.and(PublicacionSpecification.filtrarPorUsuario(usuarioId));
        }

        if ("recent".equals(sort)) {
            LocalDateTime haceUnDia = LocalDateTime.now().minusDays(1);
            spec = spec.and(PublicacionSpecification.filtrarPorFechaDesde(haceUnDia));
        }

        String sortProperty = "fechaCreacion";
        if ("recent".equals(sort)) {
            sortProperty = "fechaCreacion";
        } else if ("popular".equals(sort)) {
            // Para popular, no usar sortProperty aquí
            sortProperty = null;
        } else if (sort != null && !sort.isEmpty()) {
            sortProperty = sort;
        }

        Pageable pageable;
        if ("popular".equals(sort)) {
            pageable = PageRequest.of(page, size);
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortProperty));
        }

        Page<Publicacion> publicaciones;

        if ("popular".equals(sort)) {
            final Specification<Publicacion> finalSpec = spec; // evitar problema de efectividad final

            publicaciones = publicacionRepositorio.findAll((root, query, cb) -> {
                query.distinct(true);
                root.fetch("autor"); // opcional si necesitas los datos del autor

                // Subconsulta de likes
                Subquery<Long> likesSubquery = query.subquery(Long.class);
                Root<UsuarioLikePublicacion> likesRoot = likesSubquery.from(UsuarioLikePublicacion.class);
                likesSubquery.select(cb.count(likesRoot));
                likesSubquery.where(cb.equal(likesRoot.get("id").get("publicacion").get("id"), root.get("id")));

                // Subconsulta de comentarios
                Subquery<Long> comentariosSubquery = query.subquery(Long.class);
                Root<Comentario> comentariosRoot = comentariosSubquery.from(Comentario.class);
                comentariosSubquery.select(cb.count(comentariosRoot));
                comentariosSubquery.where(cb.equal(comentariosRoot.get("publicacion").get("id"), root.get("id")));

                query.orderBy(cb.desc(cb.sum(likesSubquery.getSelection(), comentariosSubquery.getSelection())));

                return finalSpec.toPredicate(root, query, cb);
            }, pageable);
        } else {
            publicaciones = publicacionRepositorio.findAll(spec, pageable);
        }

        Page<PublicacionDTO> dtos = publicaciones.map(publicacionMapper::toDto);

        dtos.forEach(dto -> {
            LocalDateTime fecha = dto.getFechaPublicacion();
            if (fecha == null && dto.getId() != null) {
                fecha = publicacionRepositorio.findById(dto.getId())
                        .map(Publicacion::getFechaCreacion)
                        .orElse(LocalDateTime.now());
            }
            if (fecha == null) {
                fecha = LocalDateTime.now();
            }
            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(fecha));
            dto.setTotalLikes(usuarioLikePublicacionRepositorio.countByIdPublicacionId(dto.getId()));
            dto.setTotalComentarios(comentarioServicio.contarComentariosPorPublicacion(dto.getId()));

            // Setear permisos de edición y eliminación según usuario autenticado
            if (idUsuarioAutenticado != null && dto.getUsuarioId() != null) {
                boolean esPropietario = idUsuarioAutenticado.equals(dto.getUsuarioId());
                dto.setPuedeEditar(esPropietario);
                dto.setPuedeEliminar(esPropietario);
            } else {
                dto.setPuedeEditar(false);
                dto.setPuedeEliminar(false);
            }
        });

        return dtos;
    }

    @Override
    public Optional<Publicacion> buscarPorId(Long id) {
        return publicacionRepositorio.findById(id);
    }

    @Override
    public Long obtenerIdUsuarioPorUsername(String username) {
        return usuarioServicio.buscarPorUsername(username)
                .map(usuario -> usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
