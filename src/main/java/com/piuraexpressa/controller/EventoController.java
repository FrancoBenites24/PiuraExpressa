package com.piuraexpressa.controller;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.servicio.EventoServicio;
import com.piuraexpressa.servicio.ProvinciaServicio;
import com.piuraexpressa.servicio.UsuarioEventoServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EventoController {

    private final EventoServicio eventoServicio;
    private final UsuarioEventoServicio usuarioEventoServicio;
    private final UsuarioServicio usuarioServicio;
    private final ProvinciaServicio provinciaServicio;

    @GetMapping("/eventos")
    public String mostrarEventos(@ModelAttribute("filtro") FiltroEventoDTO filtro, Model model, Principal principal) {
        Page<EventoDTO> eventos = eventoServicio.buscarEventos(filtro);

        Long usuarioId = null;
        if (principal != null) {
            usuarioId = usuarioServicio.obtenerIdPorUsername(principal.getName()).orElse(null);
        }

        for (EventoDTO evento : eventos.getContent()) {
            if (usuarioId != null) {
                boolean participa = usuarioEventoServicio.yaEstaParticipando(evento.getId(), usuarioId);
                boolean yaReseno = usuarioEventoServicio.yaResenoEvento(evento.getId(), usuarioId);

                evento.setYaParticipa(participa);
                evento.setPuedeParticipar(!participa);
                evento.setYaResenado(yaReseno);
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

            System.out.printf(
                    "Evento ID: %d | yaParticipa: %s | yaResenado: %s | capacidad: %d | participantesActuales: %d%n",
                    evento.getId(),
                    evento.getYaParticipa(),
                    evento.getYaResenado(),
                    evento.getCapacidad(),
                    evento.getParticipantesActuales());
        }

        model.addAttribute("eventos", eventos.getContent());
        model.addAttribute("paginaActual", eventos.getNumber());
        model.addAttribute("totalPaginas", eventos.getTotalPages());
        model.addAttribute("provincias", provinciaServicio.listarActivas());

        return "eventos";
    }

    @PostMapping("/eventos/{id}/participar")
    @ResponseBody
    public ResponseEntity<?> participarEvento(@PathVariable Long id, Principal principal) {
        usuarioEventoServicio.participarEnEvento(id, principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }

    @PostMapping("/eventos/{id}/cancelar")
    @ResponseBody
    public ResponseEntity<?> cancelarParticipacion(@PathVariable Long id, Principal principal) {
        usuarioEventoServicio.cancelarParticipacion(id, principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }

    @GetMapping("/eventos/{id}/resena")
    public String mostrarFormularioResena(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        Long usuarioId = usuarioServicio.obtenerIdPorUsername(principal.getName()).orElse(null);
        if (usuarioId == null || !usuarioEventoServicio.yaEstaParticipando(id, usuarioId)) {
            return "redirect:/eventos";
        }

        model.addAttribute("eventoId", id);
        return "eventos/resena";
    }
}
