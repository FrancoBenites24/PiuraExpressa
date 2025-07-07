package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivoRepositorio extends JpaRepository<Archivo, Long> {

    List<Archivo> findByReferenciaTipoAndReferenciaId(String referenciaTipo, Long referenciaId);

    List<Archivo> findByUsuarioId(Long usuarioId);
}
