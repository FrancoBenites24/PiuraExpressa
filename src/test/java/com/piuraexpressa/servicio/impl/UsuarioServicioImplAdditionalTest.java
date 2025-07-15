package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServicioImplAdditionalTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private RolRepositorio rolRepositorio;

    @InjectMocks
    private UsuarioServicioImpl usuarioServicio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testActivarUsuarioExistente() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setActivo(false);

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);

        usuarioServicio.activar(id);

        assertTrue(usuario.getActivo());
        assertNull(usuario.getFechaBaja());
        assertNull(usuario.getMotivoBaja());
        verify(usuarioRepositorio).findById(id);
        verify(usuarioRepositorio).save(usuario);
    }

    @Test
    public void testActivarUsuarioNoExistente() {
        Long id = 1L;
        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioServicio.activar(id));
        verify(usuarioRepositorio).findById(id);
        verify(usuarioRepositorio, never()).save(any());
    }

    @Test
    public void testCambiarRolUsuarioExistente() {
        Long usuarioId = 1L;
        Long rolId = 2L;
        Usuario usuario = new Usuario();
        Rol rol = new Rol();

        when(usuarioRepositorio.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(rolRepositorio.findById(rolId)).thenReturn(Optional.of(rol));
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);

        usuarioServicio.cambiarRolUsuario(usuarioId, rolId);

        assertTrue(usuario.getRoles().contains(rol));
        verify(usuarioRepositorio).findById(usuarioId);
        verify(rolRepositorio).findById(rolId);
        verify(usuarioRepositorio).save(usuario);
    }

    @Test
    public void testCambiarRolUsuarioNoUsuario() {
        Long usuarioId = 1L;
        Long rolId = 2L;

        when(usuarioRepositorio.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioServicio.cambiarRolUsuario(usuarioId, rolId));
        verify(usuarioRepositorio).findById(usuarioId);
        verify(rolRepositorio, never()).findById(any());
        verify(usuarioRepositorio, never()).save(any());
    }

    @Test
    public void testCambiarRolUsuarioNoRol() {
        Long usuarioId = 1L;
        Long rolId = 2L;
        Usuario usuario = new Usuario();

        when(usuarioRepositorio.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(rolRepositorio.findById(rolId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioServicio.cambiarRolUsuario(usuarioId, rolId));
        verify(usuarioRepositorio).findById(usuarioId);
        verify(rolRepositorio).findById(rolId);
        verify(usuarioRepositorio, never()).save(any());
    }
}
