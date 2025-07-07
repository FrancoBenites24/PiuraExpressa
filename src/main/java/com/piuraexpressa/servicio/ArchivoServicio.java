package com.piuraexpressa.servicio;

import com.piuraexpressa.model.dominio.Archivo;

import java.util.List;
import java.util.Optional;

public interface ArchivoServicio {

    Archivo guardarArchivo(Archivo archivo);

    Optional<Archivo> buscarPorId(Long id);

    List<Archivo> listarPorReferencia(String referenciaTipo, Long referenciaId);

    void eliminarArchivo(Long id);
}
