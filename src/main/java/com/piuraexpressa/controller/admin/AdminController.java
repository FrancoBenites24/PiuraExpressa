package com.piuraexpressa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String mostrarDashboard(Model modelo) {
        // Aquí puedes pasar métricas o datos relevantes al modelo si lo necesitas.
        return "admin/dashboard";
    }
}
