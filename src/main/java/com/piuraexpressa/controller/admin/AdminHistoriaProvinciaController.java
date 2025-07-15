package com.piuraexpressa.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.piuraexpressa.dto.HistoriaProvinciaDTO;
import com.piuraexpressa.servicio.HistoriaProvinciaServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/admin/provincias/{provinciaId}/historia")
@RequiredArgsConstructor
public class AdminHistoriaProvinciaController {

    private final HistoriaProvinciaServicio historiaServicio;

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_LISTAR')")
    @GetMapping
    public String listar(@PathVariable Long provinciaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoriaProvinciaDTO> historias = historiaServicio.listarPorProvincia(provinciaId, pageable);
        model.addAttribute("historias", historias);
        model.addAttribute("provinciaId", provinciaId);

        // Variables adicionales para paginación
        model.addAttribute("currentPage", historias.getNumber() + 1);
        model.addAttribute("totalPages", historias.getTotalPages());

        return "admin/provincias/historia/list";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_NUEVO')")
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@PathVariable Long provinciaId, Model model) {
        HistoriaProvinciaDTO dto = new HistoriaProvinciaDTO();
        dto.setProvinciaId(provinciaId); // Importante, para que no pierdas el contexto.
        model.addAttribute("historiaProvinciaDTO", dto);
        model.addAttribute("provinciaId", provinciaId);
        return "admin/provincias/historia/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_GUARDAR')")
    @PostMapping("/guardar")
    public String guardarHistoria(@PathVariable Long provinciaId,
            @Valid @ModelAttribute("historiaProvinciaDTO") HistoriaProvinciaDTO dto,
            BindingResult bindingResult, RedirectAttributes redirectAttrs, Model model) {
        dto.setProvinciaId(provinciaId);

        if (historiaServicio.tituloExisteEnProvincia(dto.getTitulo(), provinciaId, dto.getId())) {
            bindingResult.rejectValue("titulo", "error.titulo",
                    "Ya existe una historia con ese título en la provincia.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("provinciaId", provinciaId);
            return "admin/provincias/historia/formulario";
        }

        historiaServicio.guardar(dto);
        redirectAttrs.addFlashAttribute("success", "Historia guardada correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/historia";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_EDITAR')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long provinciaId, @PathVariable Long id, Model model,
            RedirectAttributes redirectAttrs) {
        HistoriaProvinciaDTO dto = historiaServicio.buscarPorIdYProvincia(id, provinciaId);
        if (dto == null) {
            redirectAttrs.addFlashAttribute("error", "Historia no encontrada.");
            return "redirect:/admin/provincias/" + provinciaId + "/historia";
        }
        model.addAttribute("historiaProvinciaDTO", dto);
        model.addAttribute("provinciaId", provinciaId);
        return "admin/provincias/historia/formulario";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_ACTUALIZAR')")
    @PostMapping("/actualizar/{id}")
    public String actualizarHistoria(
            @PathVariable Long provinciaId,
            @PathVariable Long id,
            @Valid @ModelAttribute("historiaProvinciaDTO") HistoriaProvinciaDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        dto.setId(id);
        dto.setProvinciaId(provinciaId);

        if (historiaServicio.tituloExisteEnProvincia(dto.getTitulo(), provinciaId, id)) {
            bindingResult.rejectValue("titulo", "error.titulo",
                    "Ya existe una historia con ese título en la provincia.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("provinciaId", provinciaId);
            model.addAttribute("modo", "editar");
            return "admin/provincias/historia/formulario";
        }

        historiaServicio.guardar(dto); // Puedes usar el mismo método porque guarda o actualiza según el id
        redirectAttrs.addFlashAttribute("success", "Historia actualizada correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/historia";
    }

    @PreAuthorize("hasAuthority('ADMINISTRAR_HISTORIA_PROVINCIA_ELIMINAR')")
    @PostMapping("/eliminar/{id}")
    public String eliminarHistoria(@PathVariable Long provinciaId, @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        historiaServicio.eliminarPorIdYProvincia(id, provinciaId);
        redirectAttrs.addFlashAttribute("success", "Historia eliminada correctamente.");
        return "redirect:/admin/provincias/" + provinciaId + "/historia";
    }
}
