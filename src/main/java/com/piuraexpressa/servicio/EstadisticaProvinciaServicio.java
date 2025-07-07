package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.model.dominio.Provincia;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstadisticaProvinciaServicio {
    Page<EstadisticaProvinciaDTO> listarPorProvincia(Long provinciaId, Pageable pageable);

    void guardar(EstadisticaProvinciaDTO dto);

    EstadisticaProvinciaDTO buscarPorIdYProvincia(Long id, Long provinciaId);

    void eliminarPorIdYProvincia(Long id, Long provinciaId);

    boolean anioExisteEnProvincia(Integer anio, Long provinciaId, Long estadisticaIdExcluir); // Para evitar duplicados
                                                                                              // en edición/creación

    Optional<EstadisticaProvinciaDTO> obtenerEstadisticaActualPorProvincia(Provincia provincia);

}