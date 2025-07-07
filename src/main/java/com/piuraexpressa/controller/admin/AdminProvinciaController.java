package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.mapper.ProvinciaMapper;
import com.piuraexpressa.servicio.ProvinciaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/provincias")
@RequiredArgsConstructor
public class AdminProvinciaController {

    private final ProvinciaServicio provinciaServicio;
    private final ProvinciaMapper provinciaMapper;

    // Listado de provincias (para la tabla)
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


    // Mostrar formulario para crear una nueva provincia
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevaProvincia(Model modelo) {
        modelo.addAttribute("provincia", new ProvinciaDTO());
        return "admin/provincias/formulario";
    }

    // Guardar provincia (nuevo o editar)
    @PostMapping("/guardar")
    public String guardarProvincia(@Valid @ModelAttribute("provincia") ProvinciaDTO provinciaDTO,
                                   org.springframework.validation.BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model modelo) {
        if (bindingResult.hasErrors()) {
            modelo.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "admin/provincias/formulario";
        }
        try {
            Provincia provincia = provinciaMapper.toEntidad(provinciaDTO);
            provinciaServicio.guardarProvincia(provincia);
            redirectAttributes.addFlashAttribute("success", "Provincia guardada exitosamente.");
            return "redirect:/admin/provincias";
        } catch (Exception e) {
            modelo.addAttribute("error", "Error al guardar: " + e.getMessage());
            return "admin/provincias/formulario";
        }
    }

    // Mostrar formulario para editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model modelo, RedirectAttributes redirectAttributes) {
        ProvinciaDTO provincia = provinciaServicio.buscarPorId(id);
        if (provincia == null) {
            redirectAttributes.addFlashAttribute("error", "Provincia no encontrada.");
            return "redirect:/admin/provincias";
        }
        modelo.addAttribute("provincia", provincia); // Ya es DTO, directo
        return "admin/provincias/editar"; // <--- archivo editar.html
    }

    // Activar provincia
    @PostMapping("/activar/{id}")
    public String activarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.activarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia activada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar: " + e.getMessage());
        }
        return "redirect:/admin/provincias";
    }

    // Desactivar provincia
    @PostMapping("/desactivar/{id}")
    public String desactivarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.desactivarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia desactivada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar: " + e.getMessage());
        }
        return "redirect:/admin/provincias";
    }

    // Eliminar provincia
    @PostMapping("/eliminar/{id}")
    public String eliminarProvincia(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            provinciaServicio.eliminarProvincia(id);
            redirectAttributes.addFlashAttribute("success", "Provincia eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/admin/provincias";
    }

    @GetMapping("/gestion/{id}")
    public String gestionProvincia(@PathVariable Long id, Model model) {
        ProvinciaDTO provincia = provinciaServicio.buscarPorId(id);
        model.addAttribute("provincia", provincia);
        return "admin/provincias/gestion";
    }

}
