package com.piuraexpressa.controller;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.servicio.EventoServicio;
import com.piuraexpressa.servicio.UsuarioEventoServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

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

    @GetMapping("/eventos")
    public String mostrarEventos(@ModelAttribute("filtro") FiltroEventoDTO filtro, Model model, Principal principal) {
        Page<?> eventos = eventoServicio.buscarEventos(filtro);

        Long usuarioId = null;
        if (principal != null) {
            usuarioId = usuarioServicio.obtenerIdPorUsername(principal.getName()).orElse(null);
        }

        for (Object obj : eventos.getContent()) {
            if (obj instanceof EventoDTO evento) {
                if (usuarioId != null) {
                    boolean participa = usuarioEventoServicio.yaEstaParticipando(evento.getId(), usuarioId);
                    evento.setYaParticipa(participa);
                    evento.setPuedeParticipar(!participa);
                } else {
                    evento.setYaParticipa(false);
                    evento.setPuedeParticipar(false);
                }

                int participantes = usuarioEventoServicio.contarParticipantes(evento.getId());
                evento.setParticipantesActuales(participantes);

                if (evento.getCapacidad() != null && evento.getCapacidad() > 0) {
                    double porcentaje = (participantes * 100.0) / evento.getCapacidad();
                    evento.setPorcentajeOcupado(porcentaje);
                } else {
                    evento.setPorcentajeOcupado(0.0);
                }

                // preubas
                System.out.println("Evento ID: " + evento.getId() + ", Ya Participa: " + evento.getYaParticipa() +
                        ", Puede Participar: " + evento.getPuedeParticipar() +
                        ", Participantes Actuales: " + evento.getParticipantesActuales() +
                        ", Capacidad: " + evento.getCapacidad() +
                        ", Porcentaje Ocupado: " + evento.getPorcentajeOcupado());
            }
        }

        model.addAttribute("eventos", eventos.getContent());
        model.addAttribute("paginaActual", eventos.getNumber());
        model.addAttribute("totalPaginas", eventos.getTotalPages());
        return "eventos"; // se espera `templates/eventos.html`
    }

    @PostMapping("/eventos/{id}/participar")
    @ResponseBody
    public ResponseEntity<?> participarEvento(@PathVariable Long id, Principal principal) {
        usuarioEventoServicio.participarEnEvento(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/eventos/{id}/cancelar")
    @ResponseBody
    public ResponseEntity<?> cancelarParticipacion(@PathVariable Long id, Principal principal) {
        usuarioEventoServicio.cancelarParticipacion(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/eventos/{id}/resena")
    public String mostrarFormularioResena(@PathVariable Long id, Model model, Principal principal) {
        // Validar que el usuario esté autenticado
        if (principal == null) {
            return "redirect:/auth/login";
        }

        // Validar que el usuario haya participado en el evento
        Long usuarioId = usuarioServicio.obtenerIdPorUsername(principal.getName()).orElse(null);
        if (usuarioId == null || !usuarioEventoServicio.yaEstaParticipando(id, usuarioId)) {
            return "redirect:/eventos";
        }

        model.addAttribute("eventoId", id);
        return "eventos/resena"; // Vista Thymeleaf para el formulario de reseña
    }
}
