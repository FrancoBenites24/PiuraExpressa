package com.piuraexpressa.dto;

import org.springframework.data.domain.Page;

import com.piuraexpressa.model.dominio.Comentario;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.model.dominio.Publicacion;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class UsuarioPerfilDTO extends UsuarioDTO {
    private Page<Publicacion> publicaciones;
    private Page<Comentario> comentarios;
    private Page<Evento> eventos;
}
