package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RolDTO;
import com.piuraexpressa.mapper.RolMapper;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.servicio.RolServicio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServicioImpl implements RolServicio {

    private final RolRepositorio rolRepositorio;
    private final RolMapper rolMapper;

    @Override
    public List<RolDTO> listarTodos() {
        return rolMapper.toDtoLista(rolRepositorio.findAll());
    }
}
