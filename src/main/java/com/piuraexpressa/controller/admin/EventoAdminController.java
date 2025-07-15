package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.EventoDTO;
import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.servicio.EventoServicio;
import com.piuraexpressa.servicio.ProvinciaServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO')")
@RequestMapping("/admin/eventos")
@RequiredArgsConstructor
public class EventoAdminController {

    private final EventoServicio eventoServicio;
    private final ProvinciaServicio provinciaServicio;
    private final UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_LISTAR')")
    @GetMapping
    public String listarEventos(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Boolean estado,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<EventoDTO> eventos = eventoServicio.listarEventos(search, estado, PageRequest.of(page, 10));
        model.addAttribute("eventos", eventos);
        model.addAttribute("search", search);
        model.addAttribute("estado", estado);
        return "admin/eventos/list";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_ACTIVAR')")
    @PostMapping("/{id}/activar")
    public String activar(@PathVariable Long id) {
        eventoServicio.activar(id);
        return "redirect:/admin/eventos";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_DESACTIVAR')")
    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        eventoServicio.desactivar(id);
        return "redirect:/admin/eventos";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_ELIMINAR')")
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        eventoServicio.eliminar(id);
        return "redirect:/admin/eventos";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_NUEVO')")
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("evento", new EventoDTO());
        model.addAttribute("provincias", provinciaServicio.listarActivas());

        return "admin/eventos/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_CREAR')")
    @PostMapping("/nuevo")
    public String crearEvento(@Valid @ModelAttribute("evento") EventoDTO eventoDTO,
            BindingResult result,
            RedirectAttributes redirect,
            Model model) {
        // Validación de fechas (mínima)
        validarFechas(eventoDTO, result);

        // Obtener el username del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Buscar ID del usuario en base a su username
        Optional<Long> idOpt = usuarioServicio.obtenerIdPorUsername(username);
        if (idOpt.isEmpty()) {
            result.reject("usuario.no.encontrado", "No se pudo obtener el usuario autenticado.");
            cargarProvincias(model);
            return "admin/eventos/formulario";
        }

        // Asignar el ID del usuario al DTO
        eventoDTO.setUsuarioId(idOpt.get());

        // Validaciones del formulario
        if (result.hasErrors()) {
            cargarProvincias(model);
            return "admin/eventos/formulario";
        }

        // Guardar el evento
        eventoServicio.crearEvento(eventoDTO);

        // Redirección con mensaje
        redirect.addFlashAttribute("success", "Evento creado correctamente.");
        return "redirect:/admin/eventos";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_EDITAR')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        EventoDTO evento = eventoServicio.obtenerPorId(id);
        if (evento == null) {
            redirect.addFlashAttribute("error", "Evento no encontrado.");
            return "redirect:/admin/eventos";
        }

        model.addAttribute("evento", evento);
        cargarProvincias(model);
        return "admin/eventos/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_EVENTO_ACTUALIZAR')")
    @PostMapping("/editar/{id}")
    public String editarEvento(@PathVariable Long id,
            @Valid @ModelAttribute("evento") EventoDTO eventoDTO,
            BindingResult result,
            RedirectAttributes redirect,
            Model model) {
        validarFechas(eventoDTO, result);

        if (result.hasErrors()) {
            cargarProvincias(model);
            return "admin/eventos/formulario";
        }

        eventoServicio.actualizarEvento(id, eventoDTO);
        redirect.addFlashAttribute("success", "Evento actualizado correctamente.");
        return "redirect:/admin/eventos";
    }

    // ===================== VALIDACIONES PERSONALIZADAS =====================
    private void validarFechas(EventoDTO eventoDTO, BindingResult result) {
        if (eventoDTO.getFechaInicio() != null && eventoDTO.getFechaFin() != null) {
            if (eventoDTO.getFechaFin().isBefore(eventoDTO.getFechaInicio())) {
                result.rejectValue("fechaFin", "fechaFin.invalida",
                        "La fecha de fin no puede ser anterior a la de inicio.");
            }
        }
    }

    private void cargarProvincias(Model model) {
        List<ProvinciaDTO> provincias = provinciaServicio.listarActivas();
        model.addAttribute("provincias", provincias);
    }
}
