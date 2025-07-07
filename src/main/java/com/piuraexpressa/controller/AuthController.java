package com.piuraexpressa.controller;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.model.dominio.TipoDocumento;
import com.piuraexpressa.servicio.ProvinciaServicio;
import com.piuraexpressa.servicio.UsuarioServicio;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ProvinciaServicio provinciaServicio;
    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model modelo) {
        modelo.addAttribute("registroDTO", new RegistroDTO());
        modelo.addAttribute("tiposDocumento", TipoDocumento.values());
        modelo.addAttribute("provincias", provinciaServicio.listarTodas());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("registroDTO") @Valid RegistroDTO dto,
            BindingResult resultado,
            Model modelo,
            RedirectAttributes redir) {
        System.out.println("🔍 Entrando a registrarUsuario con: " + dto.getUsername());

        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            resultado.rejectValue("confirmarPassword", "error.confirmarPassword", "Las contraseñas no coinciden");
        }

        if (usuarioServicio.existePorUsername(dto.getUsername())) {
            resultado.rejectValue("username", null, "El nombre de usuario ya está en uso");
        }

        if (usuarioServicio.existePorEmail(dto.getEmail())) {
            resultado.rejectValue("email", null, "El correo electrónico ya está en uso");
        }

        if (usuarioServicio.existePorNumeroDocumento(dto.getNumeroDocumento())) {
            resultado.rejectValue("numeroDocumento", null, "El número de documento ya está en uso");
        }

        if (resultado.hasErrors()) {
            System.out.println("❌ Errores de validación detectados:");
            resultado.getAllErrors().forEach(error -> System.out.println(" - " + error.toString()));

            modelo.addAttribute("tiposDocumento", TipoDocumento.values());
            modelo.addAttribute("provincias", provinciaServicio.listarTodas());
            return "auth/registro";
        }

        usuarioServicio.registrarDesdeDTO(dto);
        redir.addFlashAttribute("message", "Cuenta creada correctamente. Ahora puedes iniciar sesión.");
        return "redirect:/auth/login";
    }

    // AJAX para validación en tiempo real
    @GetMapping("/verificar-username")
    @ResponseBody
    public boolean verificarUsername(@RequestParam String username) {
        return !usuarioServicio.existePorUsername(username);
    }

    @GetMapping("/verificar-email")
    @ResponseBody
    public boolean verificarEmail(@RequestParam String email) {
        return !usuarioServicio.existePorEmail(email);
    }

    @GetMapping("/verificar-documento")
    @ResponseBody
    public boolean verificarDocumento(@RequestParam String numeroDocumento) {
        return !usuarioServicio.existePorNumeroDocumento(numeroDocumento);
    }
}
