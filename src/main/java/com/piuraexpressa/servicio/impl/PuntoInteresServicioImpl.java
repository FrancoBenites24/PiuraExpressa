package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PuntoInteresDTO;
import com.piuraexpressa.mapper.PuntoInteresMapper;
import com.piuraexpressa.model.dominio.PuntoInteres;
import com.piuraexpressa.repositorio.dominio.PuntoInteresRepositorio;
import com.piuraexpressa.servicio.PuntoInteresServicio;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuntoInteresServicioImpl implements PuntoInteresServicio {

    private final PuntoInteresRepositorio puntoRepo;
    private final PuntoInteresMapper puntoMapper;

    @Override
    public Page<PuntoInteresDTO> listarPorProvincia(Long provinciaId, Pageable pageable) {
        return puntoRepo.findByProvinciaId(provinciaId, pageable)
                .map(puntoMapper::toDto);
    }

    @Override
    public boolean nombreExisteEnProvincia(String nombre, Long provinciaId, Long idExcluir) {
        if (idExcluir != null) {
            return puntoRepo.existsByNombreIgnoreCaseAndProvinciaIdAndIdNot(nombre, provinciaId, idExcluir);
        }
        return puntoRepo.existsByNombreIgnoreCaseAndProvinciaId(nombre, provinciaId);
    }

    @Override
    public void guardar(PuntoInteresDTO dto) {
        PuntoInteres entidad = puntoMapper.toEntidad(dto);
        puntoRepo.save(entidad);
    }
    @Override
    public Optional<PuntoInteresDTO> buscarPorIdYProvincia(Long id, Long provinciaId) {
        return puntoRepo.findByIdAndProvinciaId(id, provinciaId)
            .map(puntoMapper::toDto);
    }

    @Override
    public void eliminarPorIdYProvincia(Long id, Long provinciaId) {
        puntoRepo. deleteByIdAndProvinciaId(id, provinciaId);
    }

    @Override
    public void activar(Long id, Long provinciaId) {
        puntoRepo.findByIdAndProvinciaId(id, provinciaId).ifPresent(p -> {
            p.setActivo(true);
            puntoRepo.save(p);
        });
    }

    @Override
    public void desactivar(Long id, Long provinciaId) {
        puntoRepo.findByIdAndProvinciaId(id, provinciaId).ifPresent(p -> {
            p.setActivo(false);
            puntoRepo.save(p);
        });
    }

}
