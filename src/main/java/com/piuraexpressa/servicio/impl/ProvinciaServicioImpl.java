package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.mapper.ProvinciaMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.servicio.ProvinciaServicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProvinciaServicioImpl implements ProvinciaServicio {

    private final ProvinciaRepositorio provinciaRepositorio;
    private final ProvinciaMapper provinciaMapper;

    @Override
    public Page<Provincia> listarPaginado(Pageable pageable) {
        return provinciaRepositorio.findAll(pageable);
    }

    @Override
    public Page<Provincia> buscarPorNombre(String nombre, Pageable pageable) {
        return provinciaRepositorio.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Override
    public ProvinciaDTO buscarPorId(Long id) {
        Provincia provincia = provinciaRepositorio.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada"));
        return provinciaMapper.toDto(provincia);
    }

    public Provincia buscarEntidadPorId(Long id) {
        return provinciaRepositorio.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada"));
    }

    @Override
    public void guardarProvincia(Provincia provincia) {
        provinciaRepositorio.save(provincia);
    }

    @Override
    public void activarProvincia(Long id) {
        Provincia provincia = buscarEntidadPorId(id);
        provincia.setActivo(true);
        provinciaRepositorio.save(provincia);
    }


    @Override
    public void desactivarProvincia(Long id) {
        Provincia provincia = buscarEntidadPorId(id);
        provincia.setActivo(false);
        provinciaRepositorio.save(provincia);
    }
    @Override
    public void eliminarProvincia(Long id) {
        provinciaRepositorio.deleteById(id);
    }

    @Override
    public List<Provincia> listarTodas() {
        return provinciaRepositorio.findAll();
    }

    @Override
    public Optional<Provincia> encontrarPorNombreIgnoreCase(String nombre) {
        return provinciaRepositorio.findByNombreIgnoreCase(nombre);
    }

}