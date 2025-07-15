package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RolDTO;
import com.piuraexpressa.mapper.RolMapper;
import com.piuraexpressa.model.seguridad.Permiso;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.servicio.RolServicio;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolServicioImpl implements RolServicio {

    private final RolRepositorio rolRepositorio;
    private final RolMapper rolMapper;

    @Override
    public List<RolDTO> listarTodos() {
        return rolMapper.toDtoLista(rolRepositorio.findAll());
    }

    @Override
    public Page<RolDTO> listarTodosPaginados(int pagina) {
        var pageable = PageRequest.of(pagina, 10);
        return rolRepositorio.findAll(pageable).map(rolMapper::toDto);
    }

    @Override
    public RolDTO guardarConPermisos(RolDTO rolDTO) {
        Rol rol = rolDTO.getId() != null
                ? rolRepositorio.findById(rolDTO.getId()).orElse(new Rol())
                : new Rol();

        rol.setNombre(rolDTO.getNombre());
        rol.setDescripcion(rolDTO.getDescripcion());
        rol.setActivo(rolDTO.isActivo());

        // Asignar permisos
        if (rolDTO.getPermisos() != null) {
            var permisos = new java.util.HashSet<Permiso>();
            for (Long permisoId : rolDTO.getPermisos()) {
                Permiso permiso = new Permiso();
                permiso.setId(permisoId);
                permisos.add(permiso);
            }
            rol.setPermisos(permisos);
        } else {
            rol.setPermisos(new java.util.HashSet<>());
        }

        Rol guardado = rolRepositorio.save(rol);
        return rolMapper.toDto(guardado);
    }

    @Override
    public Optional<RolDTO> buscarPorId(Long id) {
        return rolRepositorio.findById(id).map(rolMapper::toDto);
    }

    @Override
    public void eliminar(Long id) {
        rolRepositorio.deleteById(id);
    }

    @Override
    public Rol crearSiNoExiste(Rol rol) {
        Optional<Rol> rolExistente = rolRepositorio.findByNombreIgnoreCase(rol.getNombre());
        if (rolExistente.isPresent()) {
            return rolExistente.get();
        }
        return rolRepositorio.save(rol);
    }

    @Override
    public Rol guardar(Rol rol) {
        return rolRepositorio.save(rol);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepositorio.findByNombreIgnoreCase(nombre);
    }
}
