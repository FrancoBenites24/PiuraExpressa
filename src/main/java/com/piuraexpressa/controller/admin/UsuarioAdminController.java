package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.servicio.RolServicio;
import com.piuraexpressa.servicio.ProvinciaServicio;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMINISTRAR_USUARIOS')")
public class UsuarioAdminController {

    private final UsuarioServicio usuarioServicio;
    private final RolServicio rolServicio;
    private final ProvinciaServicio provinciaServicio;

    @GetMapping
    public String listarUsuarios(@RequestParam(required = false) String filtro,
            @RequestParam(required = false) String provincia,
            @PageableDefault(size = 10) Pageable pageable,
            Model modelo) {
        modelo.addAttribute("usuarios", usuarioServicio.buscarUsuariosPaginados(filtro, provincia, pageable));
        modelo.addAttribute("filtro", filtro);
        modelo.addAttribute("provincias", provinciaServicio.listarTodas());
        return "admin/usuarios/list";
    }

    @PreAuthorize("hasAuthority('MOSTRAR_DETALLE_USUARIOS')")
    @GetMapping("/{id}")
    public String mostrarDetalleUsuario(@PathVariable Long id, Model modelo) {
        Optional<UsuarioDTO> usuarioOpt = usuarioServicio.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/admin/usuarios?error=usuario_no_encontrado";
        }

        UsuarioDTO usuario = usuarioOpt.get();
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", rolServicio.listarTodos());
        
        // Obtener el ID del rol principal del usuario
        // Necesitamos obtener esto desde el servicio ya que el DTO no tiene toda la información
        Long usuarioRolId = usuarioServicio.obtenerRolPrincipalId(id);
        modelo.addAttribute("usuarioRolId", usuarioRolId);
        return "admin/usuarios/detalle";
    }

    @PreAuthorize("hasAuthority('CAMBIAR_ROL_USUARIO')")
    @PostMapping("/{id}/cambiar-rol")
    public String cambiarRolUsuario(@PathVariable Long id,
            @RequestParam("rolId") Long nuevoRolId,
            RedirectAttributes redir) {
        usuarioServicio.cambiarRolUsuario(id, nuevoRolId);
        redir.addFlashAttribute("message", "Rol actualizado correctamente.");
        return "redirect:/admin/usuarios/" + id;
    }

    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIO')")
    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id) {
        usuarioServicio.desactivar(id);
        return "redirect:/admin/usuarios";
    }

    @PreAuthorize("hasAuthority('ACTIVAR_USUARIO')")
    @PostMapping("/{id}/activar")
    public String activar(@PathVariable Long id) {
        usuarioServicio.activar(id);
        return "redirect:/admin/usuarios";
    }
}
