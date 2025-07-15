package com.piuraexpressa.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.servicio.UsuarioServicio;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/perfil")
public class PerfilVistaController {

    private final UsuarioServicio usuarioServicio;
    private final ProvinciaRepositorio provinciaRepositorio;

    public PerfilVistaController(UsuarioServicio usuarioServicio, ProvinciaRepositorio provinciaRepositorio) {
        this.usuarioServicio = usuarioServicio;
        this.provinciaRepositorio = provinciaRepositorio;
    }

    @PreAuthorize("hasAuthority('USUARIO_VER_PERFIL')")
    @GetMapping
    public String mostrarPerfil(Model model, Principal principal,
            @RequestParam(defaultValue = "0") int pubPage,
            @RequestParam(defaultValue = "0") int evtPage) {

        String username = principal.getName();

        usuarioServicio.buscarPorUsernameConPaginacion(username, pubPage, evtPage).ifPresent(perfil -> {
            model.addAttribute("usuario", perfil);
            model.addAttribute("publicacionesPage", perfil.getPublicaciones());
            model.addAttribute("comentariosPage", perfil.getComentarios());
            model.addAttribute("eventosPage", perfil.getEventos());
        });

        model.addAttribute("provincias", provinciaRepositorio.findByActivoTrueOrderByNombreAsc());

        return "perfil/index";
    }

    @PreAuthorize("hasAuthority('USUARIO_ACTUALIZAR_PERFIL')")
    @PostMapping("/actualizar")
    public String actualizarPerfil(UsuarioDTO usuarioDTO, Model model, Principal principal) {
        String usernameActual = principal.getName();

        // Validar si el nuevo username ya está en uso por otro usuario
        if (!usernameActual.equals(usuarioDTO.getUsername()) && usuarioServicio.existePorUsername(usuarioDTO.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya está en uso.");
            return mostrarPerfil(model, principal, 0, 0);
        }

        // Actualizar datos del usuario
        usuarioServicio.actualizarPerfil(usernameActual, usuarioDTO);

        return "redirect:/perfil?actualizado=true";
    }

}
