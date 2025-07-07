package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.PuntoInteres;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuntoInteresRepositorio extends JpaRepository<PuntoInteres, Long> {
    
    @EntityGraph(attributePaths = "categoria")
    Page<PuntoInteres> findByProvinciaId(Long provinciaId, Pageable pageable);

    boolean existsByNombreIgnoreCaseAndProvinciaId(String nombre, Long provinciaId);

    boolean existsByNombreIgnoreCaseAndProvinciaIdAndIdNot(String nombre, Long provinciaId, Long id);

    @EntityGraph(attributePaths = "categoria")
    Optional<PuntoInteres> findByIdAndProvinciaId(Long id, Long provinciaId);

    long countByProvincia(Provincia provincia);

    void deleteByIdAndProvinciaId(Long id, Long provinciaId);

}
