package com.piuraexpressa.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForoViewController {

@GetMapping("/foro")
public String abrirForo(Model model, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
        List<String> permisos = authentication.getAuthorities()
                                              .stream()
                                              .map(GrantedAuthority::getAuthority)
                                              .toList();
        model.addAttribute("permisos", permisos);
    }
    return "foro";
}

}
