package com.piuraexpressa.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.servicio.ProvinciaServicio;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/provincias")
@RequiredArgsConstructor
public class ProvinciaApiController {

    private final ProvinciaServicio provinciaServicio;

    @GetMapping("/todas")
    public ResponseEntity<List<ProvinciaDTO>> listarActivas() {
        return ResponseEntity.ok(provinciaServicio.listarActivas());
    }
}
