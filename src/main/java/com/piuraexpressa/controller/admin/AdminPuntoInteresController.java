package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.PuntoInteresDTO;
import com.piuraexpressa.model.dominio.CategoriaPuntoInteres;
import com.piuraexpressa.servicio.CategoriaPuntoInteresServicio;
import com.piuraexpressa.servicio.PuntoInteresServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES')")
@RequestMapping("/admin/provincias/{provinciaId}/puntos-interes")
@RequiredArgsConstructor
public class AdminPuntoInteresController {

    private final PuntoInteresServicio puntoServicio;
    private final CategoriaPuntoInteresServicio categoriaServicio;

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_LISTAR')")
    @GetMapping
    public String listar(
            @PathVariable Long provinciaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PuntoInteresDTO> puntos = puntoServicio.listarPorProvincia(provinciaId, pageable);

        model.addAttribute("puntosInteres", puntos);
        model.addAttribute("provinciaId", provinciaId);

        // Paginación
        model.addAttribute("currentPage", puntos.getNumber());
        model.addAttribute("totalPages", puntos.getTotalPages());

        return "admin/provincias/puntos-interes/list";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_NUEVO')")
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@PathVariable Long provinciaId, Model model) {
        PuntoInteresDTO dto = new PuntoInteresDTO();
        dto.setProvinciaId(provinciaId);
        model.addAttribute("puntoInteres", dto);
        model.addAttribute("provinciaId", provinciaId);
        List<CategoriaPuntoInteres> categorias = categoriaServicio.listarTodas();
        model.addAttribute("categorias", categorias);
        return "admin/provincias/puntos-interes/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_GUARDAR')")
    @PostMapping("/guardar")
    public String guardar(
            @PathVariable Long provinciaId,
            @Valid @ModelAttribute("puntoInteres") PuntoInteresDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttrs) {

        dto.setProvinciaId(provinciaId);

        if (puntoServicio.nombreExisteEnProvincia(dto.getNombre(), provinciaId, dto.getId())) {
            bindingResult.rejectValue("nombre", "error.nombre",
                    "Ya existe un punto de interés con ese nombre en la provincia.");
        }
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("provinciaId", provinciaId);
            model.addAttribute("categorias", categoriaServicio.listarTodas());
            return "admin/provincias/puntos-interes/formulario";
        }

        puntoServicio.guardar(dto);
        redirectAttrs.addFlashAttribute("success", "Punto de interés guardado correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/puntos-interes";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_EDITAR')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(
            @PathVariable Long provinciaId,
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttrs) {

        Optional<PuntoInteresDTO> optionalDto = puntoServicio.buscarPorIdYProvincia(id, provinciaId);
        if (optionalDto.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Punto de interés no encontrado.");
            return "redirect:/admin/provincias/" + provinciaId + "/puntos-interes";
        }
        PuntoInteresDTO dto = optionalDto.get();
        model.addAttribute("puntoInteres", dto);
        model.addAttribute("provinciaId", provinciaId);
        model.addAttribute("categorias", categoriaServicio.listarTodas());
        return "admin/provincias/puntos-interes/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_ELIMINAR')")
    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long provinciaId,
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        puntoServicio.eliminarPorIdYProvincia(id, provinciaId);
        redirectAttrs.addFlashAttribute("success", "Punto de interés eliminado correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/puntos-interes";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_DESACTIVAR')")
    @PostMapping("/desactivar/{id}")
    public String desactivar(
            @PathVariable Long provinciaId,
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        puntoServicio.desactivar(id, provinciaId);
        redirectAttrs.addFlashAttribute("success", "Punto de interés desactivado correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/puntos-interes";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PUNTO_INTERES_ACTIVAR')")
    @PostMapping("/activar/{id}")
    public String activar(
            @PathVariable Long provinciaId,
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        puntoServicio.activar(id, provinciaId);
        redirectAttrs.addFlashAttribute("success", "Punto de interés activado correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/puntos-interes";
    }
}
