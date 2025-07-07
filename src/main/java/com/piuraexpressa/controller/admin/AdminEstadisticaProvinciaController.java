package com.piuraexpressa.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.servicio.EstadisticaProvinciaServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/provincias/{provinciaId}/estadisticas")
@RequiredArgsConstructor
public class AdminEstadisticaProvinciaController {

    private final EstadisticaProvinciaServicio estadisticaServicio;

    @GetMapping
    public String listar(@PathVariable Long provinciaId, Model model, @PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("estadisticas", estadisticaServicio.listarPorProvincia(provinciaId, pageable));
        model.addAttribute("provinciaId", provinciaId);
        return "admin/provincias/estadisticas/list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@PathVariable Long provinciaId, Model model) {
        EstadisticaProvinciaDTO dto = new EstadisticaProvinciaDTO();
        dto.setProvinciaId(provinciaId);
        model.addAttribute("estadisticaProvinciaDTO", dto);
        model.addAttribute("provinciaId", provinciaId);
        return "admin/provincias/estadisticas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@PathVariable Long provinciaId,
                          @Valid @ModelAttribute("estadisticaProvinciaDTO") EstadisticaProvinciaDTO dto,
                          BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) {
        dto.setProvinciaId(provinciaId);

        if (estadisticaServicio.anioExisteEnProvincia(dto.getAnoActualizacion(), provinciaId, dto.getId())) {
            bindingResult.rejectValue("anoActualizacion", "error.anoActualizacion", "Ya existe una estadística para ese año en la provincia.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("provinciaId", provinciaId);
            return "admin/provincias/estadisticas/formulario";
        }

        estadisticaServicio.guardar(dto);
        redirectAttrs.addFlashAttribute("success", "Estadística guardada correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/estadisticas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long provinciaId, @PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        EstadisticaProvinciaDTO dto = estadisticaServicio.buscarPorIdYProvincia(id, provinciaId);
        if (dto == null) {
            redirectAttrs.addFlashAttribute("error", "Estadística no encontrada.");
            return "redirect:/admin/provincias/" + provinciaId + "/estadisticas";
        }
        model.addAttribute("estadisticaProvinciaDTO", dto);
        model.addAttribute("provinciaId", provinciaId);
        return "admin/provincias/estadisticas/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long provinciaId, @PathVariable Long id, RedirectAttributes redirectAttrs) {
        estadisticaServicio.eliminarPorIdYProvincia(id, provinciaId);
        redirectAttrs.addFlashAttribute("success", "Estadística eliminada correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/estadisticas";
    }
}
