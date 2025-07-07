package com.piuraexpressa.servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.piuraexpressa.dto.HistoriaProvinciaDTO;

public interface HistoriaProvinciaServicio {
    Page<HistoriaProvinciaDTO> listarPorProvincia(Long provinciaId, Pageable pageable);
    boolean tituloExisteEnProvincia(String titulo, Long provinciaId, Long idExcluir);
    void guardar(HistoriaProvinciaDTO dto);
    HistoriaProvinciaDTO buscarPorIdYProvincia(Long id, Long provinciaId);
    void eliminarPorIdYProvincia(Long id, Long provinciaId);
}
