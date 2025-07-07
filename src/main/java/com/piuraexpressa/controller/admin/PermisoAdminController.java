package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.PermisoDTO;
import com.piuraexpressa.servicio.PermisoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/permisos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMINISTRAR_USUARIOS')")
public class PermisoAdminController {

    private final PermisoServicio permisoServicio;

    // Mostrar listado
    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<PermisoDTO> permisosPage = permisoServicio.listarPaginado(PageRequest.of(page, size));
        model.addAttribute("permisosPage", permisosPage);
        return "admin/permisos/list";
    }

    // Formulario de nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("permiso", new PermisoDTO());
        return "admin/permisos/formulario";
    }

    // Formulario de editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        PermisoDTO permiso = permisoServicio.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado"));
        model.addAttribute("permiso", permiso);
        return "admin/permisos/formulario";
    }

    // Guardar (crear o actualizar)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("permiso") @Valid PermisoDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/permisos/formulario";
        }

        if (dto.getId() == null) {
            if (permisoServicio.existePorNombre(dto.getNombre())) {
                result.rejectValue("nombre", "error.permiso", "Ya existe un permiso con ese nombre.");
                return "admin/permisos/formulario";
            }
            permisoServicio.crear(dto);
        } else {
            permisoServicio.actualizar(dto.getId(), dto);
        }

        return "redirect:/admin/permisos";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        permisoServicio.eliminar(id);
        return "redirect:/admin/permisos";
    }
}
