package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.model.seguridad.Usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setActivo(usuario.getActivo());
        dto.setEmail(usuario.getEmail());
        dto.setNumeroDocumento(usuario.getNumeroDocumento());
        dto.setProvincia(usuario.getProvincia());

        // Mapear roles
        if (usuario.getRoles() != null) {
            List<String> nombresRoles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());
            dto.setRoles(nombresRoles);

            // Mapear permisos heredados desde los roles
            List<String> permisos = usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream())
                .map(permiso -> permiso.getNombre())
                .distinct()
                .collect(Collectors.toList());
            dto.setPermisos(permisos);
        }

        return dto;
    }


    public Usuario toEntidad(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setUsername(dto.getUsername());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setActivo(dto.getActivo());
        // No mapear roles aquí para evitar complejidad
        return usuario;
    }

    public List<UsuarioDTO> toDTOLista(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Usuario> toEntidadLista(List<UsuarioDTO> usuariosDto) {
        if (usuariosDto == null) {
            return null;
        }
        return usuariosDto.stream()
            .map(this::toEntidad)
            .collect(Collectors.toList());
    }
}
