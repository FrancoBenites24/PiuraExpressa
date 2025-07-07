package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.PermisoDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermisoServicio {

    List<PermisoDTO> listarTodos();

    Page<PermisoDTO> listarPaginado(Pageable pageable);

    Optional<PermisoDTO> obtenerPorId(Long id);

    Optional<PermisoDTO> obtenerPorNombre(String nombre);

    PermisoDTO crear(PermisoDTO dto);

    PermisoDTO actualizar(Long id, PermisoDTO dto);

    void eliminar(Long id);

    boolean existePorNombre(String nombre);
}
