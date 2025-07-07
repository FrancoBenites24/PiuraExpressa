package com.piuraexpressa.repositorio.seguridad;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.piuraexpressa.model.seguridad.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreIgnoreCase(String nombre);

}
