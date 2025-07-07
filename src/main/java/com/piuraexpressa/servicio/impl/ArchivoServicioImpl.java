package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.model.dominio.Archivo;
import com.piuraexpressa.repositorio.dominio.ArchivoRepositorio;
import com.piuraexpressa.servicio.ArchivoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArchivoServicioImpl implements ArchivoServicio {

    private final ArchivoRepositorio archivoRepositorio;

    @Override
    public Archivo guardarArchivo(Archivo archivo) {
        return archivoRepositorio.save(archivo);
    }

    @Override
    public Optional<Archivo> buscarPorId(Long id) {
        return archivoRepositorio.findById(id);
    }

    @Override
    public List<Archivo> listarPorReferencia(String referenciaTipo, Long referenciaId) {
        return archivoRepositorio.findByReferenciaTipoAndReferenciaId(referenciaTipo, referenciaId);
    }

    @Override
    public void eliminarArchivo(Long id) {
        archivoRepositorio.deleteById(id);
    }
}
