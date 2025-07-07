package com.piuraexpressa.config.seguridad;

import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioDetallesServicio implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<String> permisos = new HashSet<>();

        // Permisos por rol
        var roles = usuarioRolRepository.findAll().stream()
                .filter(rel -> rel.getUsuario().getId().equals(usuario.getId()))
                .map(rel -> rel.getRol().getId())
                .collect(Collectors.toSet());

        var permisosPorRol = rolPermisoRepository.findAll().stream()
                .filter(rel -> roles.contains(rel.getRol().getId()))
                .map(rel -> rel.getPermiso().getNombre())
                .toList();

        permisos.addAll(permisosPorRol);

        // Permisos por usuario
        var permisosUsuario = usuarioPermisoRecursoRepository.findAll().stream()
                .filter(p -> p.getUsuarioId().equals(usuario.getId()))
                .map(p -> p.getPermisoId()) // luego deberías mapear a nombre si deseas
                .collect(Collectors.toSet());

        // ⚠️ Si deseas el nombre del permiso, deberías consultarlo con PermisoRepositorio

        List<SimpleGrantedAuthority> authorities = permisos.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getActivo(),
                true, true, true,
                authorities
        );
    } */

    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepositorio.findByUsernameConRolesYPermisos(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    List<GrantedAuthority> authorities = usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .map(permiso -> new SimpleGrantedAuthority(permiso.getNombre()))
        .collect(Collectors.toList());

    return new User(usuario.getUsername(), usuario.getPassword(), authorities);
}

}
