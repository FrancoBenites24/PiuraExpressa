package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.EstadisticaProvincia;
import com.piuraexpressa.model.dominio.Provincia;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadisticaProvinciaRepositorio extends JpaRepository<EstadisticaProvincia, Long> {
    Page<EstadisticaProvincia> findByProvinciaId(Long provinciaId, Pageable pageable);

    boolean existsByAnoAndProvinciaId(int ano, Long provinciaId);

    boolean existsByAnoAndProvinciaIdAndIdNot(int ano, Long provinciaId, Long id);

    Optional<EstadisticaProvincia> findByIdAndProvinciaId(Long id, Long provinciaId);

    void deleteByIdAndProvinciaId(Long id, Long provinciaId);

    Optional<EstadisticaProvincia> findFirstByProvinciaOrderByAnoDesc(Provincia provincia);

    long countByProvincia(Provincia provincia);

}
