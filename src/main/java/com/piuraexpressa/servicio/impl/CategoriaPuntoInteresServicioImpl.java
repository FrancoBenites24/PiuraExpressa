package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.model.dominio.CategoriaPuntoInteres;
import com.piuraexpressa.repositorio.dominio.CategoriaPuntoInteresRepositorio;
import com.piuraexpressa.servicio.CategoriaPuntoInteresServicio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaPuntoInteresServicioImpl implements CategoriaPuntoInteresServicio {

    private final CategoriaPuntoInteresRepositorio categoriaRepo;

    public CategoriaPuntoInteresServicioImpl(CategoriaPuntoInteresRepositorio categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    @Override
    public List<CategoriaPuntoInteres> listarTodas() {
        return categoriaRepo.findAll();
    }
}
