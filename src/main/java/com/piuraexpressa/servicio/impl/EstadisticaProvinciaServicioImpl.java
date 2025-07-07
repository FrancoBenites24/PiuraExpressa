package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.EstadisticaProvinciaDTO;
import com.piuraexpressa.mapper.EstadisticaProvinciaMapper;
import com.piuraexpressa.model.dominio.EstadisticaProvincia;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.repositorio.dominio.EstadisticaProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.servicio.EstadisticaProvinciaServicio;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadisticaProvinciaServicioImpl implements EstadisticaProvinciaServicio {

    private final EstadisticaProvinciaRepositorio estadisticaRepo;
    private final ProvinciaRepositorio provinciaRepo;
    private final EstadisticaProvinciaMapper estadisticaMapper;

    @Override
    public Page<EstadisticaProvinciaDTO> listarPorProvincia(Long provinciaId, Pageable pageable) {
        return estadisticaRepo.findByProvinciaId(provinciaId, pageable).map(estadisticaMapper::toDto);
    }

    @Override
    public boolean anioExisteEnProvincia(Integer anoActualizacion, Long provinciaId, Long idExcluir) {
        if (idExcluir != null) {
            return estadisticaRepo.existsByAnoAndProvinciaIdAndIdNot(anoActualizacion, provinciaId, idExcluir);
        }
        return estadisticaRepo.existsByAnoAndProvinciaId(anoActualizacion, provinciaId);
    }

    @Override
    public void guardar(EstadisticaProvinciaDTO dto) {
        EstadisticaProvincia entidad = estadisticaMapper.toEntidad(dto);
        entidad.setProvincia(provinciaRepo.findById(dto.getProvinciaId()).orElseThrow());
        estadisticaRepo.save(entidad);
    }

    @Override
    public EstadisticaProvinciaDTO buscarPorIdYProvincia(Long id, Long provinciaId) {
        return estadisticaRepo.findByIdAndProvinciaId(id, provinciaId)
                .map(estadisticaMapper::toDto)
                .orElse(null);
    }

    @Override
    public void eliminarPorIdYProvincia(Long id, Long provinciaId) {
        estadisticaRepo.deleteByIdAndProvinciaId(id, provinciaId);
    }

    @Override
    public Optional<EstadisticaProvinciaDTO> obtenerEstadisticaActualPorProvincia(Provincia provincia) {
        Optional<EstadisticaProvincia> entidad = estadisticaRepo.findFirstByProvinciaOrderByAnoDesc(provincia);
        return entidad.map(estadisticaMapper::toDto);
    }

}
