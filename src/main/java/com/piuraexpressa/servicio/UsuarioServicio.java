package com.piuraexpressa.servicio;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.dto.UsuarioPerfilDTO;
import com.piuraexpressa.model.seguridad.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioServicio {
    Page<UsuarioDTO> listarTodos(Pageable pageable);

    Optional<UsuarioDTO> buscarPorId(Long id);

    Optional<UsuarioDTO> buscarPorEmail(String email);

    Optional<UsuarioDTO> buscarPorUsername(String username);

    Optional<UsuarioPerfilDTO> buscarPorUsernameConPaginacion(String username, int pubPage, int evtPage);

    Optional<Long> obtenerIdPorUsername(String username);

    Optional<String> obtenerNombreCompletoPorId(Long usuarioId);

    void eliminar(Long id);

    Page<UsuarioDTO> buscarUsuariosPaginados(String filtro, String provincia, Pageable pageable);

    void desactivar(Long id);

    void activar(Long id);

    void cambiarRolUsuario(Long usuarioId, Long rolId);

    boolean existePorUsername(String username);

    boolean existePorEmail(String email);

    boolean existePorNumeroDocumento(String numeroDocumento);

    void registrarDesdeDTO(RegistroDTO dto);

    Long obtenerRolPrincipalId(Long usuarioId);

    void actualizarPerfil(String usernameActual, UsuarioDTO usuarioDTO);

    Optional<Usuario> obtenerEntidadPorUsername(String username);

}
