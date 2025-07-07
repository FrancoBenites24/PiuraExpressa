package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.ProvinciaDTO;
import com.piuraexpressa.model.dominio.Provincia;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvinciaServicio {
    // Listado paginado
    Page<Provincia> listarPaginado(Pageable pageable);

    // Listado paginado con filtro por nombre (opcional, pero recomendable)
    Page<Provincia> buscarPorNombre(String nombre, Pageable pageable);

    ProvinciaDTO buscarPorId(Long id);

    void guardarProvincia(Provincia provincia);

    void guardar(ProvinciaDTO dto);

    void actualizar(ProvinciaDTO dto);

    void activarProvincia(Long id);

    void desactivarProvincia(Long id);

    void eliminarProvincia(Long id);

    List<Provincia> listarTodas();

    Optional<Provincia> encontrarPorNombreIgnoreCase(String nombre);
}
