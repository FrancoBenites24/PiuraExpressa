package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Provincia;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvinciaRepositorio extends JpaRepository<Provincia, Long> {
    Page<Provincia> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    @EntityGraph(attributePaths = {
        "historia",
        "puntosInteres",
        "puntosInteres.categoria"
    })
    Optional<Provincia> findByNombreIgnoreCase(String nombre);

    @EntityGraph(attributePaths = {
        "historia",
        "puntosInteres",
        "puntosInteres.categoria"
    })
    Optional<Provincia> findConDetallesById(Long id);

    boolean existsByNombre(String nombre);

}
