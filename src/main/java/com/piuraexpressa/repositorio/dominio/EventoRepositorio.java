package com.piuraexpressa.repositorio.dominio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.piuraexpressa.model.dominio.Evento;

public interface EventoRepositorio extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {
}
