package com.piuraexpressa.controller.admin;

import com.piuraexpressa.dto.PublicacionDTO;
import com.piuraexpressa.servicio.PublicacionServicio;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/publicaciones")
@RequiredArgsConstructor
public class PublicacionAdminController {

    private final PublicacionServicio publicacionServicio;

    @GetMapping
    public String listarPublicaciones(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "filtro", required = false) String filtro,
            Model model) {

        Long idUsuarioAutenticado = null;
        boolean isAdmin = false;

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                idUsuarioAutenticado = publicacionServicio.obtenerIdUsuarioPorUsername(username);
                isAdmin = auth.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            }
        } catch (Exception e) {
            // Ignorar, valores por defecto ya establecidos
        }

        Page<PublicacionDTO> publicaciones = publicacionServicio
                .buscarPublicaciones(page, 10, filtro, search, null, idUsuarioAutenticado, isAdmin);

        model.addAttribute("filtro", filtro);
        model.addAttribute("publicaciones", publicaciones);
        model.addAttribute("search", search);

        return "admin/publicaciones/list";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPublicacion(
            @PathVariable Long id,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirect) {

        String username = null;

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                username = auth.getName();
            }
        } catch (Exception e) {
            // Ignorar
        }

        try {
            publicacionServicio.eliminarPublicacion(id, username);
            redirect.addFlashAttribute("success", "Publicación eliminada correctamente.");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        // Preservar filtros y búsqueda al volver a la lista
        return "redirect:/admin/publicaciones?page=" + page +
                "&search=" + (search != null ? search : "") +
                "&filtro=" + (filtro != null ? filtro : "");
    }
}
