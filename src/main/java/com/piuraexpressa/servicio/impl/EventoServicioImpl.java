package com.piuraexpressa.servicio.impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.mapper.EventoMapper;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.repositorio.dominio.EventoRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;
import com.piuraexpressa.servicio.EventoServicio;
import com.piuraexpressa.specification.EventoSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventoServicioImpl implements EventoServicio {

    private final EventoRepositorio eventoRepositorio;
    private final ProvinciaRepositorio provinciaRepositorio;
    private final EventoMapper eventoMapper;
    private final ResenaRepositorio resenaRepositorio;

    @Override
    public Page<EventoDTO> buscarEventos(FiltroEventoDTO filtro) {
        Pageable pageable = PageRequest.of(filtro.getPagina(), filtro.getTamanio(), Sort.by("fechaInicio").ascending());
        Specification<Evento> spec = EventoSpecification.filtrar(filtro);

        return eventoRepositorio.findAll(spec, pageable)
                .map(evento -> eventoMapper.toDto(evento, resenaRepositorio));
    }

    @Override
    public Page<EventoDTO> listarEventos(String texto, Boolean activo, Pageable pageable) {
        Page<Evento> pagina = eventoRepositorio.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();

            if (texto != null && !texto.isBlank()) {
                var titulo = cb.like(cb.lower(root.get("titulo")), "%" + texto.toLowerCase() + "%");
                var descripcion = cb.like(cb.lower(root.get("descripcion")), "%" + texto.toLowerCase() + "%");
                predicates = cb.and(predicates, cb.or(titulo, descripcion));
            }

            if (activo != null) {
                predicates = cb.and(predicates, cb.equal(root.get("activo"), activo));
            }

            return predicates;
        }, pageable);

        return pagina.map(evento -> eventoMapper.toDto(evento, resenaRepositorio));
    }

    @Override
    public void activar(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        evento.setActivo(true);
        evento.setFechaBaja(null);
        evento.setMotivoBaja(null);
        eventoRepositorio.save(evento);
    }

    @Override
    public void desactivar(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        evento.setActivo(false);
        evento.setFechaBaja(LocalDateTime.now());
        evento.setMotivoBaja("Desactivado por administrador");
        eventoRepositorio.save(evento);
    }

    @Override
    public void eliminar(Long id) {
        if (!eventoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("Evento no encontrado");
        }
        eventoRepositorio.deleteById(id);
    }

    @Override
    public void crearEvento(EventoDTO dto) {
        Evento entidad = eventoMapper.toEntidad(dto);
        entidad.setActivo(true);
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setFechaActualizacion(LocalDateTime.now());

        if (dto.getProvincia() != null) {
            Provincia provincia = provinciaRepositorio.findByNombreIgnoreCase(dto.getProvincia())
                    .orElseThrow(() -> new IllegalArgumentException("Provincia no encontrada: " + dto.getProvincia()));
            entidad.setProvincia(provincia);
        }

        eventoRepositorio.save(entidad);
    }

    @Override
    public void actualizarEvento(Long id, EventoDTO dto) {
        Evento existente = eventoRepositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento no encontrado con ID: " + id));

        Evento actualizado = eventoMapper.toEntidad(dto);
        actualizado.setId(id);
        actualizado.setActivo(existente.getActivo());
        actualizado.setFechaCreacion(existente.getFechaCreacion());
        actualizado.setFechaActualizacion(LocalDateTime.now());
        actualizado.setUsuarioId(existente.getUsuarioId());

        if (dto.getProvincia() != null) {
            Provincia provincia = provinciaRepositorio.findByNombreIgnoreCase(dto.getProvincia())
                    .orElseThrow(() -> new IllegalArgumentException("Provincia no encontrada: " + dto.getProvincia()));
            actualizado.setProvincia(provincia);
        }

        eventoRepositorio.save(actualizado);
    }

    @Override
    public EventoDTO obtenerPorId(Long id) {
        Evento evento = eventoRepositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evento no encontrado con ID: " + id));
        return eventoMapper.toDto(evento, resenaRepositorio);
    }
}
