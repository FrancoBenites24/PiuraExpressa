package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.PublicacionMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.repositorio.dominio.ReportePublicacionRepositorio;
import com.piuraexpressa.servicio.ComentarioServicio;
import com.piuraexpressa.servicio.PublicacionServicio;
import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.specification.PublicacionSpecification;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
    private final ReportePublicacionRepositorio reportePublicacionRepositorio;

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

        // 6. Generar slug y asegurar unicidad asegurando que al editar el slug
        // pertenezca a la misam publicadion
        String baseSlug = generarSlug(dto.getTitulo());
        String slug = baseSlug;
        int suffix = 1;

        // Evitar colisión con otros slugs menos en de la msma publicaicon
        while (publicacionRepositorio.findBySlug(slug)
                .filter(p -> !p.getId().equals(publicacion.getId()))
                .isPresent()) {
            slug = baseSlug + "-" + suffix;
            suffix++;
        }
        publicacion.setSlug(slug);

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
            publicacion.setActivo(true); // or true, depending on default desired behavior
        }

        // 4. Actualizar provincia si corresponde
        if (dto.getProvinciaId() != null) {
            Provincia provincia = provinciaRepositorio.findById(dto.getProvinciaId())
                    .orElseThrow(() -> new RuntimeException("Provincia no encontrada"));
            publicacion.setProvincia(provincia);
        } else {
            publicacion.setProvincia(null);
        }

        // 5. Actualizar slug con restriccion de unicidad y
        // evitar errores al actualizar porque es de la mimsma publicacion
        String baseSlug = generarSlug(dto.getTitulo());
        String slug = baseSlug;
        int suffix = 1;

        // Asegurarse que el slug no exista en otra publicación
        while (publicacionRepositorio.findBySlug(slug)
                .filter(p -> !p.getId().equals(publicacion.getId()))
                .isPresent()) {
            slug = baseSlug + "-" + suffix;
            suffix++;
        }
        publicacion.setSlug(slug);

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

        // Verificar si el usuario es admin
        boolean esAdmin = usuario.getRoles().stream()
                .anyMatch(rol -> "ROLE_ADMIN".equals(rol));

        if (!esAdmin && !publicacion.getAutor().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado para eliminar esta publicación");
        }

        publicacionRepositorio.delete(publicacion);
    }

    @Override
    public Page<PublicacionDTO> buscarPublicaciones(int page, int size, String sort, String search, Long usuarioId,
            Long idUsuarioAutenticado, boolean isAdmin) {
        Specification<Publicacion> spec = (root, query, cb) -> cb.conjunction();

        // 🔐 Solo publicaciones activas
        spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), true));

        String filtroSort = (sort != null) ? sort.trim().toLowerCase() : "";

        // Filtro por usuario específico
        if (usuarioId != null) {
            spec = spec.and(PublicacionSpecification.filtrarPorUsuario(usuarioId));
        }

        // Filtro "recientes"
        if ("recent".equals(filtroSort)) {
            LocalDateTime haceUnDia = LocalDateTime.now().minusHours(24);
            spec = spec.and(PublicacionSpecification.filtrarPorFechaDesde(haceUnDia));
        }

        Pageable pageable;
        int offset = page * size;

        if ("popular".equals(filtroSort)) {
            List<Long> ids = publicacionRepositorio.findPublicacionesPopulares(size, offset);
            List<Publicacion> publicaciones = ids.isEmpty()
                    ? Collections.emptyList()
                    : publicacionRepositorio.findAllById(ids);
            long total = publicacionRepositorio.contarPublicacionesActivas();
            return mapearADTO(new PageImpl<>(publicaciones, PageRequest.of(page, size), total), idUsuarioAutenticado,
                    isAdmin);
        }

        else if ("mas_comentados".equals(filtroSort)) {
            List<Long> ids = publicacionRepositorio.findMasComentadas(size, offset);
            List<Publicacion> publicaciones = ids.isEmpty()
                    ? Collections.emptyList()
                    : publicacionRepositorio.findAllById(ids);
            long total = publicacionRepositorio.contarPublicacionesActivas();
            return mapearADTO(new PageImpl<>(publicaciones, PageRequest.of(page, size), total), idUsuarioAutenticado,
                    isAdmin);
        }

        else if ("mayores_reportes".equals(filtroSort)) {
            List<Long> ids = publicacionRepositorio.findMasReportadas(size, offset);
            List<Publicacion> publicaciones = ids.isEmpty()
                    ? Collections.emptyList()
                    : publicacionRepositorio.findAllById(ids);
            long total = publicacionRepositorio.contarPublicacionesActivas();
            return mapearADTO(new PageImpl<>(publicaciones, PageRequest.of(page, size), total), idUsuarioAutenticado,
                    isAdmin);
        }

        else if ("mas_likeados".equals(filtroSort)) {
            List<Long> ids = publicacionRepositorio.findMasLikeadas(size, offset);
            List<Publicacion> publicaciones = ids.isEmpty()
                    ? Collections.emptyList()
                    : publicacionRepositorio.findAllById(ids);
            long total = publicacionRepositorio.contarPublicacionesActivas();
            return mapearADTO(new PageImpl<>(publicaciones, PageRequest.of(page, size), total), idUsuarioAutenticado,
                    isAdmin);
        }

        else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaCreacion"));
            Page<Publicacion> publicaciones = publicacionRepositorio.findAll(spec, pageable);
            return mapearADTO(publicaciones, idUsuarioAutenticado, isAdmin);
        }

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

    private Page<PublicacionDTO> mapearADTO(Page<Publicacion> publicaciones, Long idUsuarioAutenticado,
            boolean isAdmin) {
        Page<PublicacionDTO> dtos = publicaciones.map(publicacionMapper::toDto);

        dtos.forEach(dto -> {
            LocalDateTime fecha = dto.getFechaPublicacion();
            if (fecha == null && dto.getId() != null) {
                fecha = publicacionRepositorio.findById(dto.getId())
                        .map(Publicacion::getFechaCreacion)
                        .orElse(LocalDateTime.now());
            }

            dto.setTiempoTranscurrido(calcularTiempoTranscurrido(fecha));
            dto.setTotalLikes(usuarioLikePublicacionRepositorio.countByIdPublicacionId(dto.getId()));
            dto.setTotalComentarios(comentarioServicio.contarComentariosPorPublicacion(dto.getId()));

            if (dto.getContenido() != null) {
                dto.setContenidoResumen(dto.getContenido().length() > 100
                        ? dto.getContenido().substring(0, 100) + "..."
                        : dto.getContenido());
            } else {
                dto.setContenidoResumen("");
            }

            dto.setTotalReportes(reportePublicacionRepositorio.countByPublicacionId(dto.getId()));

            // Permisos
            if (isAdmin) {
                dto.setPuedeEditar(false); // o true si deseas que edite también
                dto.setPuedeEliminar(true);
            } else if (idUsuarioAutenticado != null) {
                boolean esPropietario = dto.getUsuarioId() != null && idUsuarioAutenticado.equals(dto.getUsuarioId());
                dto.setPuedeEditar(esPropietario); // permitir edición si es dueño
                dto.setPuedeEliminar(esPropietario);
            } else {
                dto.setPuedeEditar(false);
                dto.setPuedeEliminar(false);
            }
        });

        return dtos;
    }

    @Override
    public Optional<UsuarioDTO> buscarUsuarioPorUsername(String username) {
        return usuarioServicio.buscarPorUsername(username);
    }

}
