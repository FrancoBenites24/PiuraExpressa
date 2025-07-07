package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RolPermisoDTO;
import com.piuraexpressa.mapper.RolPermisoMapper;
import com.piuraexpressa.model.seguridad.Permiso;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.model.seguridad.RolPermiso;
import com.piuraexpressa.model.seguridad.RolPermisoId;
import com.piuraexpressa.repositorio.seguridad.PermisoRepositorio;
import com.piuraexpressa.repositorio.seguridad.RolPermisoRepositorio;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.servicio.RolPermisoServicio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolPermisoServicioImpl implements RolPermisoServicio {

    private final RolRepositorio rolRepositorio;
    private final PermisoRepositorio permisoRepositorio;
    private final RolPermisoRepositorio rolPermisoRepositorio;
    private final RolPermisoMapper rolPermisoMapper;

    @Override
    public void asignarPermisoARol(RolPermisoDTO dto) {
        Rol rol = rolRepositorio.findById(dto.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        Permiso permiso = permisoRepositorio.findById(dto.getPermisoId())
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado"));

        RolPermisoId id = new RolPermisoId(dto.getRolId(), dto.getPermisoId());

        RolPermiso entidad = new RolPermiso();
        entidad.setId(id);
        entidad.setRol(rol);
        entidad.setPermiso(permiso);

        rolPermisoRepositorio.save(entidad);
    }

    @Override
    public void eliminarPermisoDeRol(Long rolId, Long permisoId) {
        RolPermisoId id = new RolPermisoId(rolId, permisoId);
        rolPermisoRepositorio.deleteById(id);
    }

    @Override
    public List<RolPermisoDTO> listarTodos() {
        return rolPermisoMapper.toDtoLista(rolPermisoRepositorio.findAll());
    }

    @Override
    public RolPermisoDTO obtenerPorId(Long rolId, Long permisoId) {
        RolPermiso entidad = rolPermisoRepositorio.findById(new RolPermisoId(rolId, permisoId))
                .orElseThrow(() -> new EntityNotFoundException("Asignación Rol-Permiso no encontrada"));
        return rolPermisoMapper.toDto(entidad);
    }
}
