package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.dto.UsuarioPerfilDTO;
import com.piuraexpressa.mapper.UsuarioMapper;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.repositorio.dominio.ComentarioRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.repositorio.dominio.UsuarioEventoRepositorio;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UsuarioServicioImplTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private ProvinciaRepositorio provinciaRepositorio;

    @Mock
    private ComentarioRepositorio comentarioRepositorio;

    @Mock
    private UsuarioEventoRepositorio usuarioEventoRepositorio;

    @Mock
    private PublicacionRepositorio publicacionRepositorio;

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
    void testBuscarPorId_conRelaciones() {
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setUsername("usuario1");

        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setId(userId);
        dto.setUsername("usuario1");

        when(usuarioRepositorio.findById(userId)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(dto);

        when(publicacionRepositorio.findByAutor(eq(userId), any())).thenReturn(Page.empty());
        when(publicacionRepositorio.countByAutor(userId)).thenReturn(0L);

        when(comentarioRepositorio.findByUsuarioId(eq(userId), any())).thenReturn(Page.empty());
        when(comentarioRepositorio.countByUsuarioId(userId)).thenReturn(0L);

        when(usuarioEventoRepositorio.findByIdUsuarioId(eq(userId), any())).thenReturn(Page.empty());
        when(usuarioEventoRepositorio.countByIdUsuarioId(userId)).thenReturn(0);

        Optional<UsuarioDTO> resultado = usuarioServicio.buscarPorId(userId);

        assertTrue(resultado.isPresent());

        UsuarioPerfilDTO actual = (UsuarioPerfilDTO) resultado.get();
        assertEquals(userId, actual.getId());
        assertEquals("usuario1", actual.getUsername());
        assertEquals(0, actual.getTotalPublicaciones());
        assertEquals(0, actual.getTotalComentarios());
        assertEquals(0, actual.getTotalEventosAsistidos());

        verify(usuarioRepositorio).findById(userId);
        verify(publicacionRepositorio).findByAutor(eq(userId), any());
        verify(comentarioRepositorio).findByUsuarioId(eq(userId), any());
        verify(usuarioEventoRepositorio).findByIdUsuarioId(eq(userId), any());
    }

    @Test
    void testBuscarPorUsernameConPaginacion() {
        Long userId = 1L;
        String username = "usuario1";
        Usuario usuario = new Usuario();
        usuario.setId(userId);
        usuario.setUsername(username);

        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setId(userId);
        dto.setUsername(username);

        when(usuarioRepositorio.findByUsername(username)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(any(Usuario.class))).thenReturn(dto);

        when(publicacionRepositorio.findByAutor(eq(userId), any())).thenReturn(Page.empty());
        when(publicacionRepositorio.countByAutor(userId)).thenReturn(0L);

        when(comentarioRepositorio.findByUsuarioId(eq(userId), any())).thenReturn(Page.empty());
        when(comentarioRepositorio.countByUsuarioId(userId)).thenReturn(0L);

        when(usuarioEventoRepositorio.findByIdUsuarioId(eq(userId), any())).thenReturn(Page.empty());
        when(usuarioEventoRepositorio.countByIdUsuarioId(userId)).thenReturn(0);

        Optional<UsuarioPerfilDTO> result = usuarioServicio.buscarPorUsernameConPaginacion(username, 0, 0);

        assertTrue(result.isPresent());

        UsuarioPerfilDTO actual = result.get();
        assertEquals(userId, actual.getId());
        assertEquals(username, actual.getUsername());
        assertEquals(0, actual.getTotalPublicaciones());
        assertEquals(0, actual.getTotalComentarios());
        assertEquals(0, actual.getTotalEventosAsistidos());

        verify(usuarioRepositorio).findByUsername(username);
        verify(publicacionRepositorio).findByAutor(eq(userId), any());
        verify(comentarioRepositorio).findByUsuarioId(eq(userId), any());
        verify(usuarioEventoRepositorio).findByIdUsuarioId(eq(userId), any());
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

        assertFalse(usuario.getActivo());
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
        dto.setPassword("Password1!");
        dto.setNombres("Nombre");
        dto.setApellidos("Apellido");
        dto.setTipoDocumento("DNI");
        dto.setNumeroDocumento("12345678");
        dto.setTelefono("912345678");
        dto.setDireccion("Direccion");
        dto.setDistrito("Distrito");
        dto.setFechaNacimiento(java.time.LocalDate.of(2000, 10, 10));
        dto.setProvinciaId(null);

        when(provinciaRepositorio.findById(any())).thenReturn(Optional.empty());
        when(rolRepositorio.findByNombreIgnoreCase("USUARIO")).thenReturn(Optional.of(new Rol()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepositorio.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioServicio.registrarDesdeDTO(dto);

        verify(provinciaRepositorio, never()).findById(any());
        verify(rolRepositorio).findByNombreIgnoreCase("USUARIO");
        verify(passwordEncoder).encode("Password1!");
        verify(usuarioRepositorio).save(any(Usuario.class));
    }
}
