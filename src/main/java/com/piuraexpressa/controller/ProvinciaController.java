package com.piuraexpressa.controller;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.mapper.ProvinciaMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.servicio.EstadisticaProvinciaServicio;
import com.piuraexpressa.servicio.ProvinciaServicio;
import com.piuraexpressa.repositorio.dominio.PuntoInteresRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaServicio provinciaServicio;
    private final EstadisticaProvinciaServicio estadisticaProvinciaServicio;
    private final PuntoInteresRepositorio puntoInteresRepositorio;
    private final ProvinciaMapper provinciaMapper;

    @GetMapping("/provincias/{nombre}")
    public String mostrarDetalleProvincia(@PathVariable String nombre, Model modelo) {
        // Buscar por nombre ignorando mayúsculas/minúsculas
        Provincia provincia = provinciaServicio.encontrarPorNombreIgnoreCase(nombre).orElse(null);
        if (provincia == null) {
            return "error/404";
        }

        ProvinciaDTO provinciaDTO = provinciaMapper.toDto(provincia);
        modelo.addAttribute("provincia", provinciaDTO);

        // --- Cambia a Optional<EstadisticaProvinciaDTO> ---
        Optional<EstadisticaProvinciaDTO> estadisticaActual = estadisticaProvinciaServicio
                .obtenerEstadisticaActualPorProvincia(provincia);

        modelo.addAttribute("estadisticaActual", estadisticaActual.orElse(null));

        // Si tienes un método en tu servicio para contar puntos de interés, usa el ID o
        // el DTO, NO la entidad
        long totalPuntosInteres = puntoInteresRepositorio.countByProvincia(provincia);
        modelo.addAttribute("totalPuntosInteres", totalPuntosInteres);

        // Agregar provinciasNavbar para el dropdown del navbar
        modelo.addAttribute("provinciasNavbar", provinciaServicio.listarTodas());

        // Si tienes métodos para obtener historia y puntos de interés, pásalos como DTO
        // Si ya están embebidos en el ProvinciaDTO, perfecto:
        // Se eliminan estas líneas para evitar duplicidad y conflicto con la plantilla
        // modelo.addAttribute("historia", provinciaDTO.getHistoria());
        // modelo.addAttribute("puntosInteres", provinciaDTO.getPuntosInteres());

        return "detalle-provincia";
    }

    @GetMapping("/provincias")
    public String mostrarProvincias(Model modelo) {
        modelo.addAttribute("provincias", provinciaServicio.listarTodas());
        return "provincias";
    }
}
