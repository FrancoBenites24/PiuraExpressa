package com.piuraexpressa.repositorio.seguridad;

import com.piuraexpressa.model.seguridad.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoRepositorio extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
