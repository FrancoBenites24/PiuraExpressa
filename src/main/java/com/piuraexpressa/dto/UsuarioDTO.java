package com.piuraexpressa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioDTO {

    private Long id;
    private Boolean activo;

    private String username;

    private String email;

    private String telefono;

    private String provincia;

    private String direccion;

    private String numeroDocumento;

    private List<String> roles;

    private String nombres;

    private String apellidos;

    private Integer totalPublicaciones;

    private Integer totalComentarios;

    private Integer totalEventosAsistidos;

    private List<String> permisos;
}
