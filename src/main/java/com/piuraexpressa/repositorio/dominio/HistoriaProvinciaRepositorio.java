package com.piuraexpressa.repositorio.dominio;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.piuraexpressa.model.dominio.HistoriaProvincia;

public interface HistoriaProvinciaRepositorio extends JpaRepository<HistoriaProvincia, Long> {
    Page<HistoriaProvincia> findByProvinciaIdAndActivoTrue(Long provinciaId, Pageable pageable);
    boolean existsByTituloIgnoreCaseAndProvinciaId(String titulo, Long provinciaId);
    boolean existsByTituloIgnoreCaseAndProvinciaIdAndIdNot(String titulo, Long provinciaId, Long id);
    Optional<HistoriaProvincia> findByIdAndProvinciaId(Long id, Long provinciaId);
    void deleteByIdAndProvinciaId(Long id, Long provinciaId);
}
