package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.PermisoDTO;

import java.util.List;
import java.util.Optional;

public interface PermisoServicio {

    List<PermisoDTO> listarTodos();

    Optional<PermisoDTO> obtenerPorId(Long id);

    Optional<PermisoDTO> obtenerPorNombre(String nombre);

    PermisoDTO crear(PermisoDTO dto);

    PermisoDTO actualizar(Long id, PermisoDTO dto);

    void eliminar(Long id);

    boolean existePorNombre(String nombre);
}
