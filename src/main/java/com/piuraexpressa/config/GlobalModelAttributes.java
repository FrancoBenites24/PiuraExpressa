package com.piuraexpressa.config;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.servicio.ProvinciaServicio;
import com.piuraexpressa.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private ProvinciaServicio provinciaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @ModelAttribute
    public void agregarAtributosGlobales(Model model, Principal principal) {
        // Cargar provincias activas
        List<ProvinciaDTO> provincias = provinciaServicio.listarActivas();
        model.addAttribute("provincias", provincias);

        // Cargar usuario autenticado
        if (principal != null) {
            Optional<Usuario> usuarioOpt = usuarioServicio.obtenerEntidadPorUsername(principal.getName());
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
}
