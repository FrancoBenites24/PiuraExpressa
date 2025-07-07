package com.piuraexpressa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForoViewController {

    @GetMapping("/foro")
    public String abrirForo() {
        return "foro";
    }
}
