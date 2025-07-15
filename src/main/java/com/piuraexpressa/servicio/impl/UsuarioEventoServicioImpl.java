package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.model.dominio.UsuarioEvento;
import com.piuraexpressa.model.dominio.UsuarioEventoId;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;
import com.piuraexpressa.repositorio.dominio.UsuarioEventoRepositorio;
import com.piuraexpressa.servicio.UsuarioEventoServicio;
import com.piuraexpressa.servicio.UsuarioServicio;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioEventoServicioImpl implements UsuarioEventoServicio {

    private final UsuarioEventoRepositorio usuarioEventoRepositorio;
    private final UsuarioServicio usuarioServicio;
    private final ResenaRepositorio resenaRepositorio;

    @Override
    @Transactional("dominioTransactionManager")
    public void participarEnEvento(Long eventoId, String username) {
        Long usuarioId = usuarioServicio.obtenerIdPorUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UsuarioEventoId id = new UsuarioEventoId(usuarioId, eventoId);

        if (usuarioEventoRepositorio.existsById(id)) {
            return;
        }

        UsuarioEvento relacion = new UsuarioEvento();
        relacion.setId(id);
        relacion.setFechaRegistro(LocalDateTime.now());
        relacion.setConfirmado(true); // puede ajustarse a lógica futura
        relacion.setObservaciones(null); // en caso luego lo implementes

        usuarioEventoRepositorio.save(relacion);
    }

    @Override
    @Transactional("dominioTransactionManager")
    public void cancelarParticipacion(Long eventoId, String username) {
        Long usuarioId = usuarioServicio.obtenerIdPorUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        UsuarioEventoId id = new UsuarioEventoId(usuarioId, eventoId);
        usuarioEventoRepositorio.deleteById(id);
    }

    @Override
    public boolean yaEstaParticipando(Long eventoId, Long usuarioId) {
        return usuarioEventoRepositorio.existsByIdUsuarioIdAndIdEventoId(usuarioId, eventoId);
    }

    @Override
    public int contarParticipantes(Long eventoId) {
        return usuarioEventoRepositorio.countByIdEventoId(eventoId);
    }

    @Override
    public boolean yaResenoEvento(Long eventoId, Long usuarioId) {
        return resenaRepositorio.existsByEventoIdAndUsuarioIdAndActivaTrue(eventoId, usuarioId);
    }

}
