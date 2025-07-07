package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.PuntoInteresDTO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PuntoInteresServicio {
    Page<PuntoInteresDTO> listarPorProvincia(Long provinciaId, Pageable pageable);

    boolean nombreExisteEnProvincia(String nombre, Long provinciaId, Long idExcluir);

    void guardar(PuntoInteresDTO dto);

    Optional<PuntoInteresDTO> buscarPorIdYProvincia(Long id, Long provinciaId);

    void eliminarPorIdYProvincia(Long id, Long provinciaId);

    void activar(Long id, Long provinciaId);

    void desactivar(Long id, Long provinciaId);
}
