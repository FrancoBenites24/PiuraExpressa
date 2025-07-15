package com.piuraexpressa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String accesoDenegado(Model model) {
        return "error/403";
    }
}
