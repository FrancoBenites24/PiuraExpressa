package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.ResenaDTO;
import com.piuraexpressa.mapper.ResenaMapper;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.model.dominio.Resena;
import com.piuraexpressa.repositorio.dominio.EventoRepositorio;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import com.piuraexpressa.servicio.ResenaServicio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaServicioImpl implements ResenaServicio {

    private final ResenaRepositorio resenaRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final ResenaMapper resenaMapper;
    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    @Transactional(transactionManager = "dominioTransactionManager")
    public ResenaDTO guardarResena(ResenaDTO dto) {
        Long eventoId = dto.getEventoId();

        Evento evento = eventoRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        Resena resena = resenaMapper.toEntidad(dto);
        resena.setEvento(evento);
        resena.setFechaResena(LocalDateTime.now());
        resena.setActiva(true);

        Resena guardada = resenaRepositorio.save(resena);
        return resenaMapper.toDto(guardada);
    }

    @Override
    public List<ResenaDTO> obtenerResenasPorEvento(Long eventoId) {
        List<Resena> resenas = resenaRepositorio.findAll().stream()
                .filter(r -> r.getEvento().getId().equals(eventoId) && r.isActiva())
                .toList();

        List<ResenaDTO> resenasDTO = new ArrayList<>();
        for (Resena r : resenas) {
            ResenaDTO dto = resenaMapper.toDto(r);
            dto.setNombreUsuario(
                    usuarioRepositorio.findById(r.getUsuarioId())
                            .map(u -> u.getNombres() + " " + u.getApellidos())
                            .orElse("Usuario desconocido"));
            dto.setFechaRegistro(r.getFechaResena());
            resenasDTO.add(dto);
        }

        return resenasDTO;
    }

}
