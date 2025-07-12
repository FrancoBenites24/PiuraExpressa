
package com.piuraexpressa.controller.api;

import com.piuraexpressa.dto.ComentarioDTO;
import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.servicio.ComentarioServicio;
import com.piuraexpressa.servicio.EventoServicio;
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
    private final ComentarioServicio comentarioServicio;
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

    @PostMapping("/{id}/resena")
    public ResponseEntity<?> crearResena(@PathVariable Long id, @RequestBody ComentarioDTO comentarioDTO) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        ComentarioDTO createdComentario = comentarioServicio.crearComentario(id, comentarioDTO, username);
        return ResponseEntity.ok(createdComentario);
    }
}
