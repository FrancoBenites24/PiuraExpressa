package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.servicio.ProvinciaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/admin/provincias")
@RequiredArgsConstructor
public class AdminProvinciaController {

    private final ProvinciaServicio provinciaServicio;

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_LISTAR')")
    @GetMapping
    public String listarProvincias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String buscar,
            Model modelo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Provincia> provincias;
        if (buscar != null && !buscar.isBlank()) {
            provincias = provinciaServicio.buscarPorNombre(buscar, pageable);
            modelo.addAttribute("buscar", buscar);
        } else {
            provincias = provinciaServicio.listarPaginado(pageable);
        }
        modelo.addAttribute("provincias", provincias);
        return "admin/provincias/list";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_NUEVO')")
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaProvincia(Model modelo) {
        modelo.addAttribute("provincia", new ProvinciaDTO());
        modelo.addAttribute("modo", "nuevo");
        return "admin/provincias/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_GUARDAR')")
    @PostMapping("/guardar")
    public String guardarProvincia(@Valid @ModelAttribute("provincia") ProvinciaDTO provinciaDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model modelo) {
        if (provinciaDTO.getId() != null) {
            return "redirect:/admin/provincias/actualizar/" + provinciaDTO.getId(); // Seguridad defensiva
        }

        if (bindingResult.hasErrors()) {
            modelo.addAttribute("error", "Por favor corrige los errores del formulario.");
            modelo.addAttribute("modo", "nuevo");
            return "admin/provincias/formulario";
        }

        try {
            provinciaServicio.guardar(provinciaDTO);
            redirectAttributes.addFlashAttribute("success", "Provincia registrada exitosamente.");
            return "redirect:/admin/provincias";
        } catch (Exception e) {
            modelo.addAttribute("error", "Error al guardar: " + e.getMessage());
            modelo.addAttribute("modo", "nuevo");
            return "admin/provincias/formulario";
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_EDITAR')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model modelo,
            RedirectAttributes redirectAttributes) {
        ProvinciaDTO provincia = provinciaServicio.buscarPorId(id);
        if (provincia == null) {
            redirectAttributes.addFlashAttribute("error", "Provincia no encontrada.");
            return "redirect:/admin/provincias";
        }
        modelo.addAttribute("provincia", provincia);
        modelo.addAttribute("modo", "editar");
        return "admin/provincias/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_ACTUALIZAR')")
    @PostMapping("/actualizar/{id}")
    public String actualizarProvincia(@PathVariable Long id,
            @Valid @ModelAttribute("provincia") ProvinciaDTO provinciaDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model modelo) {
        if (bindingResult.hasErrors()) {
            modelo.addAttribute("error", "Por favor corrige los errores del formulario.");
            modelo.addAttribute("modo", "editar");
            return "admin/provincias/formulario";
        }

        try {
            provinciaDTO.setId(id); // aseguramos consistencia
            provinciaServicio.actualizar(provinciaDTO);
            redirectAttributes.addFlashAttribute("success", "Provincia actualizada exitosamente.");
            return "redirect:/admin/provincias";
        } catch (Exception e) {
            modelo.addAttribute("error", "Error al actualizar: " + e.getMessage());
            modelo.addAttribute("modo", "editar");
            return "admin/provincias/formulario";
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_ACTIVAR')")
    @PostMapping("{id}/activar")
    public String activarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.activarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia activada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar: " + e.getMessage());
        }
        return "redirect:/admin/provincias";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_DESACTIVAR')")
    @PostMapping("{id}/desactivar")
    public String desactivarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.desactivarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia desactivada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar: " + e.getMessage());
        }
        return "redirect:/admin/provincias";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_ELIMINAR')")
    @PostMapping("/eliminar/{id}")
    public String eliminarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.eliminarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia eliminada correctamente.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado al eliminar la provincia.");
        }
        return "redirect:/admin/provincias";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_PROVINCIA_GESTION')")
    @GetMapping("/gestion/{id}")
    public String gestionProvincia(@PathVariable Long id, Model model) {
        ProvinciaDTO provincia = provinciaServicio.buscarPorId(id);
        model.addAttribute("provincia", provincia);
        return "admin/provincias/gestion";
    }

}
