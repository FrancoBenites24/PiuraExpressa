package com.piuraexpressa.servicio.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.piuraexpressa.dto.HistoriaProvinciaDTO;
import com.piuraexpressa.mapper.HistoriaProvinciaMapper;
import com.piuraexpressa.model.dominio.HistoriaProvincia;
import com.piuraexpressa.repositorio.dominio.HistoriaProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.servicio.HistoriaProvinciaServicio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoriaProvinciaServicioImpl implements HistoriaProvinciaServicio {

    private final HistoriaProvinciaRepositorio historiaRepo;
    private final ProvinciaRepositorio provinciaRepo;
    private final HistoriaProvinciaMapper historiaMapper;

    @Override
    public Page<HistoriaProvinciaDTO> listarPorProvincia(Long provinciaId, Pageable pageable) {
        return historiaRepo.findByProvinciaIdAndActivoTrue(provinciaId, pageable).map(historiaMapper::toDto);
    }

    @Override
    public boolean tituloExisteEnProvincia(String titulo, Long provinciaId, Long idExcluir) {
        if (idExcluir != null) {
            return historiaRepo.existsByTituloIgnoreCaseAndProvinciaIdAndIdNot(titulo, provinciaId, idExcluir);
        }
        return historiaRepo.existsByTituloIgnoreCaseAndProvinciaId(titulo, provinciaId);
    }

    @Override
    public void guardar(HistoriaProvinciaDTO dto) {
        HistoriaProvincia entidad = historiaMapper.toEntidad(dto);
        entidad.setProvincia(provinciaRepo.findById(dto.getProvinciaId()).orElseThrow());
        historiaRepo.save(entidad);
    }

    @Override
    public HistoriaProvinciaDTO buscarPorIdYProvincia(Long id, Long provinciaId) {
        return historiaRepo.findByIdAndProvinciaId(id, provinciaId)
                .map(historiaMapper::toDto)
                .orElse(null);
    }

    @Override
    public void eliminarPorIdYProvincia(Long id, Long provinciaId) {
        historiaRepo.deleteByIdAndProvinciaId(id, provinciaId);
    }
}
