package com.piuraexpressa.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.piuraexpressa.servicio.UsuarioServicio;

@Controller
@RequestMapping("/perfil")
public class PerfilVistaController {

    private final UsuarioServicio usuarioServicio;

    public PerfilVistaController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public String mostrarPerfil(Model model, Principal principal) {
        String username = principal.getName();
        usuarioServicio.buscarPorUsername(username).ifPresent(usuarioDto ->
            model.addAttribute("usuario", usuarioDto)
        );
        return "perfil/index";
    }
}
