package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.CategoriaPuntoInteres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaPuntoInteresRepositorio extends JpaRepository<CategoriaPuntoInteres, Long> {

    // Buscar por nombre, ignorando mayúsculas/minúsculas
    Optional<CategoriaPuntoInteres> findByNombreIgnoreCase(String nombre);

    // Verificar si existe una categoría con ese nombre (evita duplicados)
    boolean existsByNombreIgnoreCase(String nombre);

    // Verificar si existe otro con ese nombre, excluyendo un id (para edición)
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    // Listar por nombre parcial y activas, útil para autocompletado
    Page<CategoriaPuntoInteres> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
