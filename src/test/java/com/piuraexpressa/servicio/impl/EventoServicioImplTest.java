package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.mapper.EventoMapper;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.repositorio.dominio.EventoRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventoServicioImplTest {

    @Mock private EventoRepositorio eventoRepositorio;
    @Mock private ProvinciaRepositorio provinciaRepositorio;
    @Mock private EventoMapper eventoMapper;
    @Mock private ResenaRepositorio resenaRepositorio;

    @InjectMocks private EventoServicioImpl eventoServicio;

    @Test
    void testCrearEvento_ProvinciaExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setProvincia("Piura");

        Evento evento = new Evento();
        Provincia provincia = new Provincia();

        when(eventoMapper.toEntidad(dto)).thenReturn(evento);
        when(provinciaRepositorio.findByNombreIgnoreCase("Piura")).thenReturn(Optional.of(provincia));
        when(eventoRepositorio.save(evento)).thenReturn(evento);

        eventoServicio.crearEvento(dto);

        assertTrue(evento.getActivo());
        assertNotNull(evento.getFechaCreacion());
        assertNotNull(evento.getFechaActualizacion());
        assertEquals(provincia, evento.getProvincia());

        verify(eventoMapper).toEntidad(dto);
        verify(provinciaRepositorio).findByNombreIgnoreCase("Piura");
        verify(eventoRepositorio).save(evento);
    }

    @Test
    void testCrearEvento_ProvinciaNoExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setProvincia("Desconocida");

        Evento evento = new Evento();

        when(eventoMapper.toEntidad(dto)).thenReturn(evento);
        when(provinciaRepositorio.findByNombreIgnoreCase("Desconocida")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventoServicio.crearEvento(dto));

        verify(provinciaRepositorio).findByNombreIgnoreCase("Desconocida");
        verify(eventoRepositorio, never()).save(any());
    }

    @Test
    void testActualizarEventoExistente() {
        Long id = 1L;
        EventoDTO dto = new EventoDTO();
        dto.setProvincia("Piura");

        Evento existente = new Evento();
        existente.setId(id);
        existente.setActivo(true);
        existente.setFechaCreacion(LocalDateTime.now().minusDays(1));
        existente.setUsuarioId(10L);

        Evento actualizado = new Evento();
        Provincia provincia = new Provincia();

        when(eventoRepositorio.findById(id)).thenReturn(Optional.of(existente));
        when(eventoMapper.toEntidad(dto)).thenReturn(actualizado);
        when(provinciaRepositorio.findByNombreIgnoreCase("Piura")).thenReturn(Optional.of(provincia));
        when(eventoRepositorio.save(actualizado)).thenReturn(actualizado);

        eventoServicio.actualizarEvento(id, dto);

        assertEquals(id, actualizado.getId());
        assertEquals(existente.getActivo(), actualizado.getActivo());
        assertEquals(existente.getFechaCreacion(), actualizado.getFechaCreacion());
        assertEquals(existente.getUsuarioId(), actualizado.getUsuarioId());
        assertEquals(provincia, actualizado.getProvincia());
        assertNotNull(actualizado.getFechaActualizacion());

        verify(eventoRepositorio).findById(id);
        verify(provinciaRepositorio).findByNombreIgnoreCase("Piura");
        verify(eventoMapper).toEntidad(dto);
        verify(eventoRepositorio).save(actualizado);
    }

    @Test
    void testActualizarEventoNoExistente() {
        Long id = 1L;
        EventoDTO dto = new EventoDTO();

        when(eventoRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> eventoServicio.actualizarEvento(id, dto));

        verify(eventoRepositorio).findById(id);
        verify(eventoRepositorio, never()).save(any());
    }

    @Test
    void testEliminarEventoExistente() {
        Long id = 1L;
        when(eventoRepositorio.existsById(id)).thenReturn(true);

        eventoServicio.eliminar(id);

        verify(eventoRepositorio).existsById(id);
        verify(eventoRepositorio).deleteById(id);
    }

    @Test
    void testEliminarEventoNoExistente() {
        Long id = 1L;
        when(eventoRepositorio.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> eventoServicio.eliminar(id));

        verify(eventoRepositorio).existsById(id);
        verify(eventoRepositorio, never()).deleteById(any());
    }

    @Test
    void testActivarEventoExistente() {
        Long id = 1L;
        Evento evento = new Evento();
        evento.setActivo(false);

        when(eventoRepositorio.findById(id)).thenReturn(Optional.of(evento));
        when(eventoRepositorio.save(evento)).thenReturn(evento);

        eventoServicio.activar(id);

        assertTrue(evento.getActivo());
        assertNull(evento.getFechaBaja());
        assertNull(evento.getMotivoBaja());

        verify(eventoRepositorio).findById(id);
        verify(eventoRepositorio).save(evento);
    }

    @Test
    void testActivarEventoNoExistente() {
        Long id = 1L;
        when(eventoRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventoServicio.activar(id));

        verify(eventoRepositorio).findById(id);
        verify(eventoRepositorio, never()).save(any());
    }

    @Test
    void testDesactivarEventoExistente() {
        Long id = 1L;
        Evento evento = new Evento();
        evento.setActivo(true);

        when(eventoRepositorio.findById(id)).thenReturn(Optional.of(evento));
        when(eventoRepositorio.save(evento)).thenReturn(evento);

        eventoServicio.desactivar(id);

        assertFalse(evento.getActivo());
        assertNotNull(evento.getFechaBaja());
        assertEquals("Desactivado por administrador", evento.getMotivoBaja());

        verify(eventoRepositorio).findById(id);
        verify(eventoRepositorio).save(evento);
    }

    @Test
    void testDesactivarEventoNoExistente() {
        Long id = 1L;
        when(eventoRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventoServicio.desactivar(id));

        verify(eventoRepositorio).findById(id);
        verify(eventoRepositorio, never()).save(any());
    }

    @Test
    void testObtenerPorIdExistente() {
        Long id = 1L;
        Evento evento = new Evento();
        EventoDTO dto = new EventoDTO();

        when(eventoRepositorio.findById(id)).thenReturn(Optional.of(evento));
        when(eventoMapper.toDto(evento, resenaRepositorio)).thenReturn(dto);

        EventoDTO resultado = eventoServicio.obtenerPorId(id);

        assertNotNull(resultado);
        assertEquals(dto, resultado);

        verify(eventoRepositorio).findById(id);
        verify(eventoMapper).toDto(evento, resenaRepositorio);
    }

    @Test
    void testObtenerPorIdNoExistente() {
        Long id = 1L;
        when(eventoRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> eventoServicio.obtenerPorId(id));

        verify(eventoRepositorio).findById(id);
        verify(eventoMapper, never()).toDto(any(), any());
    }
}
