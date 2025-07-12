package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.mapper.HistoriaProvinciaMapper;
import com.piuraexpressa.mapper.ProvinciaMapper;
import com.piuraexpressa.mapper.PuntoInteresMapper;
import com.piuraexpressa.model.dominio.HistoriaProvincia;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.PuntoInteres;
import com.piuraexpressa.repositorio.dominio.EstadisticaProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.servicio.ProvinciaServicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProvinciaServicioImpl implements ProvinciaServicio {

    private final ProvinciaRepositorio provinciaRepositorio;
    private final ProvinciaMapper provinciaMapper;
    private final EstadisticaProvinciaRepositorio estadisticaRepo;

    @Autowired
    private ProvinciaMapper mapper;

    @Autowired
    private HistoriaProvinciaMapper historiaProvinciaMapper;

    @Autowired
    PuntoInteresMapper puntoInteresMapper;

    @Override
    public Page<Provincia> listarPaginado(Pageable pageable) {
        return provinciaRepositorio.findAll(pageable);
    }

    @Override
    public Page<Provincia> buscarPorNombre(String nombre, Pageable pageable) {
        return provinciaRepositorio.findByNombreContainingIgnoreCase(nombre, pageable);
    }

    @Transactional(readOnly = true, transactionManager = "dominioTransactionManager")
    @Override
    public ProvinciaDTO buscarPorId(Long id) {
        Provincia provincia = provinciaRepositorio.findConDetallesById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada"));
        return provinciaMapper.toDto(provincia);
    }

    public Provincia buscarEntidadPorId(Long id) {
        return provinciaRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada"));
    }

    @Override
    @Transactional(transactionManager = "dominioTransactionManager")
    public void guardar(ProvinciaDTO dto) {
        if (provinciaRepositorio.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una provincia con ese nombre.");
        }
        Provincia entidad = mapper.toEntidad(dto);
        provinciaRepositorio.save(entidad);
    }

    @Override
    @Transactional(transactionManager = "dominioTransactionManager")
    public void actualizar(ProvinciaDTO dto) {
        Provincia existente = provinciaRepositorio.findConDetallesById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada."));

        if (!dto.getNombre().equalsIgnoreCase(existente.getNombre()) &&
                provinciaRepositorio.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe otra provincia con ese nombre.");
        }
        mapper.actualizarEntidadDesdeDto(dto, existente);

        // No reemplaces directamente las listas. Limpia y añade.
        if (dto.getHistoria() != null) {
            existente.getHistoria().clear();
            existente.getHistoria().addAll(dto.getHistoria().stream()
                    .map(h -> {
                        HistoriaProvincia entidad = historiaProvinciaMapper.toEntidad(h);
                        entidad.setProvincia(existente);
                        return entidad;
                    }).toList());
        }

        if (dto.getPuntosInteres() != null) {
            existente.getPuntosInteres().clear();
            existente.getPuntosInteres().addAll(dto.getPuntosInteres().stream()
                    .map(p -> {
                        PuntoInteres entidad = puntoInteresMapper.toEntidad(p);
                        entidad.setProvincia(existente);
                        return entidad;
                    }).toList());
        }

        provinciaRepositorio.save(existente);
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

    @Transactional(transactionManager = "dominioTransactionManager")
    @Override
    public void eliminarProvincia(Long id) {
        Provincia provincia = buscarEntidadPorId(id);

        long tieneHistoria = provincia.getHistoria().size();
        long tienePuntos = provincia.getPuntosInteres().size();
        long tieneEstadisticas = estadisticaRepo.countByProvincia(provincia);

        if (tieneHistoria > 0 || tienePuntos > 0 || tieneEstadisticas > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar: existen historias, puntos de interés o estadísticas vinculadas.");
        }

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

    @Override
    public void guardarProvincia(Provincia provincia) {
        provinciaRepositorio.save(provincia);
    }

    // filtro api
    @Override
    public List<ProvinciaDTO> listarActivas() {
        return provinciaRepositorio.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(provinciaMapper::toDto)
                .toList();
    }

}