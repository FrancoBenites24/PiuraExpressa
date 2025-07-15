package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.RolDTO;
import com.piuraexpressa.dto.PermisoDTO;
import com.piuraexpressa.servicio.RolServicio;
import com.piuraexpressa.servicio.PermisoServicio;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RolAdminController {

    private final RolServicio rolServicio;
    private final PermisoServicio permisoServicio;

    @GetMapping
    public String listarRoles(@RequestParam(defaultValue = "0") int page, Model model) {
        var rolesPaginados = rolServicio.listarTodosPaginados(page);

        // Enriquecer cada rol con la lista de PermisoDTO a partir de sus IDs
        var rolesConPermisos = rolesPaginados.map(rol -> {
            var permisosDetallados = permisoServicio.obtenerPorIds(rol.getPermisos());
            rol.setPermisosDetallados(permisosDetallados);
            return rol;
        });

        model.addAttribute("roles", rolesConPermisos);
        return "admin/roles/list";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("rol", new RolDTO());
        var permisos = permisoServicio.listarTodos();
        var permisosPorRecurso = permisos.stream()
                .collect(Collectors.groupingBy(PermisoDTO::getRecurso));
        model.addAttribute("permisosPorRecurso", permisosPorRecurso);
        return "admin/roles/form";
    }

    @PostMapping("/crear")
    public String crearRol(@ModelAttribute RolDTO rolDTO) {
        rolServicio.guardarConPermisos(rolDTO);
        return "redirect:/admin/roles";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var rolOpt = rolServicio.buscarPorId(id);
        if (rolOpt.isEmpty()) {
            return "redirect:/admin/roles?error=rol_no_encontrado";
        }
        model.addAttribute("rol", rolOpt.get());
        var permisos = permisoServicio.listarTodos();
        var permisosPorRecurso = permisos.stream()
                .collect(Collectors.groupingBy(PermisoDTO::getRecurso));
        model.addAttribute("permisosPorRecurso", permisosPorRecurso);
        return "admin/roles/form";
    }

    @PostMapping("/editar/{id}")
    public String editarRol(@PathVariable Long id, @ModelAttribute RolDTO rolDTO) {
        rolDTO.setId(id);
        rolServicio.guardarConPermisos(rolDTO);
        return "redirect:/admin/roles";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarRol(@PathVariable Long id) {
        rolServicio.eliminar(id);
        return "redirect:/admin/roles";
    }
}
