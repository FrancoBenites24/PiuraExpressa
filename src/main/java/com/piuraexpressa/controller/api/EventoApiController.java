package com.piuraexpressa.controller.api;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.dto.ResenaDTO;
import com.piuraexpressa.servicio.EventoServicio;
import com.piuraexpressa.servicio.ResenaServicio;
import com.piuraexpressa.servicio.UsuarioEventoServicio;
import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.repositorio.dominio.ResenaRepositorio;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoApiController {

    private final EventoServicio eventoServicio;
    private final UsuarioEventoServicio usuarioEventoServicio;
    private final UsuarioServicio usuarioServicio;
    private final ResenaServicio resenaServicio;
    private final ResenaRepositorio resenaRepositorio;

    @PostMapping("/buscar")
    public ResponseEntity<Page<EventoDTO>> buscarEventos(@RequestBody FiltroEventoDTO filtro) {
        Page<EventoDTO> eventos = eventoServicio.buscarEventos(filtro);

        String username = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            username = auth.getName();
        }

        Long usuarioId = null;
        if (username != null) {
            usuarioId = usuarioServicio.obtenerIdPorUsername(username).orElse(null);
        }

        for (EventoDTO evento : eventos) {
            if (usuarioId != null) {
                boolean participa = usuarioEventoServicio.yaEstaParticipando(evento.getId(), usuarioId);
                evento.setYaParticipa(participa);
                evento.setPuedeParticipar(!participa);
                boolean resenado = false;
                try {
                    resenado = resenaRepositorio.existsByUsuarioIdAndEventoId(usuarioId, evento.getId());
                } catch (Exception e) {
                    resenado = false;
                }
                evento.setYaResenado(resenado);
            } else {
                evento.setYaParticipa(false);
                evento.setPuedeParticipar(false);
                evento.setYaResenado(false);
            }

            int participantes = usuarioEventoServicio.contarParticipantes(evento.getId());
            evento.setParticipantesActuales(participantes);

            if (evento.getCapacidad() != null && evento.getCapacidad() > 0) {
                double porcentaje = (participantes * 100.0) / evento.getCapacidad();
                evento.setPorcentajeOcupado(porcentaje);
            } else {
                evento.setPorcentajeOcupado(0.0);
            }
        }

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerEventoPorId(@PathVariable Long id) {
        EventoDTO evento = eventoServicio.obtenerPorId(id);

        // Obtener nombre de usuario actual
        String username = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            username = auth.getName();
        }

        Long usuarioId = null;
        if (username != null) {
            usuarioId = usuarioServicio.obtenerIdPorUsername(username).orElse(null);
        }

        if (usuarioId != null) {
            boolean participa = usuarioEventoServicio.yaEstaParticipando(evento.getId(), usuarioId);
            evento.setYaParticipa(participa);
            evento.setPuedeParticipar(!participa);
            boolean resenado = resenaRepositorio.existsByUsuarioIdAndEventoId(usuarioId, evento.getId());
            evento.setYaResenado(resenado);
        } else {
            evento.setYaParticipa(false);
            evento.setPuedeParticipar(false);
            evento.setYaResenado(false);
        }

        int participantes = usuarioEventoServicio.contarParticipantes(evento.getId());
        evento.setParticipantesActuales(participantes);

        if (evento.getCapacidad() != null && evento.getCapacidad() > 0) {
            double porcentaje = (participantes * 100.0) / evento.getCapacidad();
            evento.setPorcentajeOcupado(porcentaje);
        } else {
            evento.setPorcentajeOcupado(0.0);
        }
        evento.setResenas(resenaServicio.obtenerResenasPorEvento(evento.getId()));

        return ResponseEntity.ok(evento);
    }

    @PostMapping("/{id}/resena")
    public ResponseEntity<?> crearResena(@PathVariable Long id, @RequestBody ResenaDTO resenaDTO) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        String username = auth.getName();
        Long usuarioId = usuarioServicio.obtenerIdPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        resenaDTO.setUsuarioId(usuarioId);
        resenaDTO.setEventoId(id); // o setEventoId(id) si renombras el campo

        ResenaDTO createdResena = resenaServicio.guardarResena(resenaDTO);
        return ResponseEntity.ok(createdResena);
    }
}
