package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.PermisoDTO;
import com.piuraexpressa.mapper.PermisoMapper;
import com.piuraexpressa.model.seguridad.Permiso;
import com.piuraexpressa.repositorio.seguridad.PermisoRepositorio;
import com.piuraexpressa.servicio.PermisoServicio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermisoServicioImpl implements PermisoServicio {

    private final PermisoRepositorio permisoRepositorio;
    private final PermisoMapper permisoMapper;

    @Override
    public List<PermisoDTO> listarTodos() {
        return permisoMapper.toDtoLista(permisoRepositorio.findAll());
    }

    @Override
    public Optional<PermisoDTO> obtenerPorId(Long id) {
        return permisoRepositorio.findById(id).map(permisoMapper::toDto);
    }

    @Override
    public Optional<PermisoDTO> obtenerPorNombre(String nombre) {
        return permisoRepositorio.findByNombreIgnoreCase(nombre).map(permisoMapper::toDto);
    }

    @Override
    public PermisoDTO crear(PermisoDTO dto) {
        Permiso permiso = permisoMapper.toEntidad(dto);
        return permisoMapper.toDto(permisoRepositorio.save(permiso));
    }

    @Override
    public PermisoDTO actualizar(Long id, PermisoDTO dto) {
        Permiso permiso = permisoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado"));
        permisoMapper.actualizarEntidadDesdeDto(dto, permiso);
        return permisoMapper.toDto(permisoRepositorio.save(permiso));
    }

    @Override
    public void eliminar(Long id) {
        permisoRepositorio.deleteById(id);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return permisoRepositorio.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public Page<PermisoDTO> listarPaginado(Pageable pageable) {
        return permisoRepositorio.findAll(pageable)
                .map(permisoMapper::toDto);
    }
}
