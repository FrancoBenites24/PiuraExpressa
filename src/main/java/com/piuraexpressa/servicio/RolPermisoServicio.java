package com.piuraexpressa.servicio;

import java.util.List;

import com.piuraexpressa.dto.RolPermisoDTO;

public interface RolPermisoServicio {
    void asignarPermisoARol(RolPermisoDTO dto);
    void eliminarPermisoDeRol(Long rolId, Long permisoId);
    List<RolPermisoDTO> listarTodos();
    RolPermisoDTO obtenerPorId(Long rolId, Long permisoId);
}
