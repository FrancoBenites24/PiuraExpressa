package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.UsuarioMapper;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServicioImplTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private ProvinciaRepositorio provinciaRepositorio;

    @Mock
    private RolRepositorio rolRepositorio;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServicioImpl usuarioServicio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        Usuario usuario = new Usuario();
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        Page<Usuario> pageUsuarios = new PageImpl<>(Collections.singletonList(usuario));
        when(usuarioRepositorio.findAll(pageable)).thenReturn(pageUsuarios);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        Page<UsuarioDTO> result = usuarioServicio.listarTodos(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usuarioRepositorio).findAll(pageable);
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    public void testBuscarPorId() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        Optional<UsuarioDTO> result = usuarioServicio.buscarPorId(id);

        assertTrue(result.isPresent());
        assertEquals(usuarioDTO, result.get());
        verify(usuarioRepositorio).findById(id);
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    public void testEliminar() {
        Long id = 1L;
        doNothing().when(usuarioRepositorio).deleteById(id);

        usuarioServicio.eliminar(id);

        verify(usuarioRepositorio).deleteById(id);
    }

    @Test
    public void testDesactivarUsuarioExistente() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setActivo(true);

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);

        usuarioServicio.desactivar(id);

        assertFalse(usuario.isActivo());
        assertNotNull(usuario.getFechaBaja());
        assertEquals("Desactivado manualmente", usuario.getMotivoBaja());
        verify(usuarioRepositorio).findById(id);
        verify(usuarioRepositorio).save(usuario);
    }

    @Test
    public void testDesactivarUsuarioNoExistente() {
        Long id = 1L;
        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioServicio.desactivar(id));
        verify(usuarioRepositorio).findById(id);
        verify(usuarioRepositorio, never()).save(any());
    }

    @Test
    public void testRegistrarDesdeDTO() {
        RegistroDTO dto = new RegistroDTO();
        dto.setUsername("usuario1");
        dto.setEmail("usuario1@example.com");
        dto.setPassword("password");
        dto.setNombres("Nombre");
        dto.setApellidos("Apellido");
        dto.setTipoDocumento("DNI");
        dto.setNumeroDocumento("12345678");
        dto.setTelefono("123456789");
        dto.setDireccion("Direccion");
        dto.setDistrito("Distrito");
        dto.setFechaNacimiento(java.time.LocalDate.of(1990, 1, 1));
        dto.setProvinciaId(null);

        when(provinciaRepositorio.findById(any())).thenReturn(Optional.empty());
        when(rolRepositorio.findByNombreIgnoreCase("USUARIO")).thenReturn(Optional.of(new Rol()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepositorio.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioServicio.registrarDesdeDTO(dto);

        verify(provinciaRepositorio, never()).findById(any());
        verify(rolRepositorio).findByNombreIgnoreCase("USUARIO");
        verify(passwordEncoder).encode("password");
        verify(usuarioRepositorio).save(any(Usuario.class));
    }
}
