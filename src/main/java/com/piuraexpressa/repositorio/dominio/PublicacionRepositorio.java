package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {
}
