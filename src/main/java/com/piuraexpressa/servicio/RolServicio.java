package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.RolDTO;
import com.piuraexpressa.model.seguridad.Rol;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface RolServicio {
    List<RolDTO> listarTodos();

    Rol guardar(Rol rol);

    Rol crearSiNoExiste(Rol rol);

    Optional<Rol> buscarPorNombre(String nombre);

    Page<RolDTO> listarTodosPaginados(int pagina);

    RolDTO guardarConPermisos(RolDTO rolDTO);

    Optional<RolDTO> buscarPorId(Long id);

    void eliminar(Long id);
}
