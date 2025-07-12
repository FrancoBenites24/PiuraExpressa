package com.piuraexpressa.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;

public interface EventoServicio {
    Page<EventoDTO> buscarEventos(FiltroEventoDTO filtro);

    Page<EventoDTO> listarEventos(String texto, Boolean activo, Pageable pageable);

    void activar(Long id);

    void desactivar(Long id);

    void eliminar(Long id);

    EventoDTO obtenerPorId(Long id);

    void crearEvento(EventoDTO dto);

    void actualizarEvento(Long id, EventoDTO dto);

}
