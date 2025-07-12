package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.mapper.UsuarioMapper;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.TipoDocumento;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import com.piuraexpressa.servicio.UsuarioServicio;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicioImpl.class);

    private final UsuarioRepositorio usuarioRepositorio;
    private final ProvinciaRepositorio provinciaRepositorio;
    private final RolRepositorio rolRepositorio;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UsuarioDTO> listarTodos(Pageable pageable) {
        return usuarioRepositorio.findAll(pageable).map(usuarioMapper::toDTO);
    }

    @Override
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).map(usuarioMapper::toDTO);
    }

    @Override
    public Optional<UsuarioDTO> buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email).map(usuarioMapper::toDTO);
    }

    @Override
    public Optional<UsuarioDTO> buscarPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username).map(usuarioMapper::toDTO);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public Page<UsuarioDTO> buscarUsuariosPaginados(String filtro, String provincia, Pageable pageable) {
        return usuarioRepositorio.buscarUsuariosFiltrados(filtro, provincia, pageable).map(usuarioMapper::toDTO);
    }

    @Override
    public void desactivar(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuario.setFechaBaja(LocalDateTime.now());
        usuario.setMotivoBaja("Desactivado manualmente");
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void activar(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuario.setFechaBaja(null);
        usuario.setMotivoBaja(null);
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void cambiarRolUsuario(Long usuarioId, Long rolId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Rol rol = rolRepositorio.findById(rolId).orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
        usuario.getRoles().clear();
        usuario.getRoles().add(rol);
        usuarioRepositorio.save(usuario);
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioRepositorio.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepositorio.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existePorNumeroDocumento(String numeroDocumento) {
        return usuarioRepositorio.existsByNumeroDocumento(numeroDocumento);
    }

    @Override
    public void registrarDesdeDTO(RegistroDTO dto) {
        logger.info("🔍 Iniciando registro de usuario: {}", dto.getUsername());

        String nombreProvincia = "Desconocido";
        if (dto.getProvinciaId() != null) {
            try {
                nombreProvincia = provinciaRepositorio.findById(dto.getProvinciaId())
                        .map(Provincia::getNombre)
                        .orElse("Desconocido");
                logger.info("🏛️ Provincia encontrada: {}", nombreProvincia);
            } catch (Exception e) {
                logger.error("❌ Error al obtener provincia: {}", e.getMessage());
                nombreProvincia = "Desconocido";
            }
        }

        // Ahora guardamos el usuario en la base de datos de seguridad
        guardarUsuarioEnBaseDatos(dto, nombreProvincia);
    }

    @Transactional(transactionManager = "seguridadTransactionManager")
    private void guardarUsuarioEnBaseDatos(RegistroDTO dto, String nombreProvincia) {
        try {
            logger.info("💾 Iniciando transacción de seguridad para usuario: {}", dto.getUsername());

            Usuario nuevo = new Usuario();
            nuevo.setUsername(dto.getUsername());
            nuevo.setEmail(dto.getEmail());
            nuevo.setPassword(passwordEncoder.encode(dto.getPassword()));
            nuevo.setNombres(dto.getNombres());
            nuevo.setApellidos(dto.getApellidos());
            nuevo.setTipoDocumento(TipoDocumento.convertirTipo(dto.getTipoDocumento()));
            nuevo.setNumeroDocumento(dto.getNumeroDocumento());
            nuevo.setTelefono(dto.getTelefono());
            nuevo.setDireccion(dto.getDireccion());
            nuevo.setDistrito(dto.getDistrito());
            nuevo.setFechaNacimiento(dto.getFechaNacimiento());
            nuevo.setActivo(true);
            nuevo.setProvincia(nombreProvincia);

            logger.info("🔍 Buscando rol USUARIO...");
            Rol rolUsuario = rolRepositorio.findByNombreIgnoreCase("USUARIO")
                    .orElseThrow(() -> new EntityNotFoundException("Rol 'USUARIO' no encontrado"));
            nuevo.getRoles().add(rolUsuario);
            logger.info("✅ Rol USUARIO asignado");

            logger.info("💾 Guardando usuario en base de datos...");
            Usuario guardado = usuarioRepositorio.save(nuevo);
            logger.info("✅ Usuario guardado exitosamente con ID: {}", guardado.getId());

        } catch (Exception e) {
            logger.error("❌ Error al guardar usuario: {}", e.getMessage(), e);
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Long obtenerRolPrincipalId(Long usuarioId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getId)
                .orElse(null);
    }

    @Override
    public Optional<Long> obtenerIdPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username)
                .map(Usuario::getId);
    }

    @Override
    public Optional<String> obtenerNombreCompletoPorId(Long usuarioId) {
        return usuarioRepositorio.findById(usuarioId)
                .map(usuario -> usuario.getNombres() + " " + usuario.getApellidos());
    }

}
