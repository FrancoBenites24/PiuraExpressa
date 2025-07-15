package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.RegistroDTO;
import com.piuraexpressa.dto.UsuarioDTO;
import com.piuraexpressa.dto.UsuarioPerfilDTO;
import com.piuraexpressa.mapper.UsuarioMapper;
import com.piuraexpressa.model.dominio.Comentario;
import com.piuraexpressa.model.dominio.Evento;
import com.piuraexpressa.model.dominio.Provincia;
import com.piuraexpressa.model.dominio.Publicacion;
import com.piuraexpressa.model.dominio.TipoDocumento;
import com.piuraexpressa.model.dominio.UsuarioEvento;
import com.piuraexpressa.model.seguridad.Usuario;
import com.piuraexpressa.model.seguridad.Rol;
import com.piuraexpressa.repositorio.dominio.ComentarioRepositorio;
import com.piuraexpressa.repositorio.dominio.ProvinciaRepositorio;
import com.piuraexpressa.repositorio.dominio.PublicacionRepositorio;
import com.piuraexpressa.repositorio.dominio.UsuarioEventoRepositorio;
import com.piuraexpressa.repositorio.dominio.EventoRepositorio;
import org.springframework.data.domain.PageImpl;
import com.piuraexpressa.repositorio.seguridad.RolRepositorio;
import com.piuraexpressa.repositorio.seguridad.UsuarioRepositorio;
import com.piuraexpressa.servicio.UsuarioServicio;
import com.piuraexpressa.util.ValidPhoneNumberValidator;

import jakarta.persistence.EntityNotFoundException;
import java.util.regex.Pattern;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private final PublicacionRepositorio publicacionRepositorio;
    private final ComentarioRepositorio comentarioRepositorio;
    private final UsuarioEventoRepositorio usuarioEventoRepositorio;
    private final EventoRepositorio eventoRepositorio;

    @Override
    public Page<UsuarioDTO> listarTodos(Pageable pageable) {
        return usuarioRepositorio.findAll(pageable).map(usuarioMapper::toDTO);
    }

    @Override
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id)
                .map(usuario -> construirPerfilDTO(usuario, 0, 0)); // primera página por defecto
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
    public Optional<UsuarioPerfilDTO> buscarPorUsernameConPaginacion(String username, int pubPage, int evtPage) {
        return usuarioRepositorio.findByUsername(username)
                .map(usuario -> construirPerfilDTO(usuario, pubPage, evtPage));
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

        // Validar que el usuario tenga al menos 16 años
        if (dto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        LocalDate fechaNacimiento = dto.getFechaNacimiento();
        LocalDate fechaMinima = LocalDate.now().minusYears(16);
        if (fechaNacimiento.isAfter(fechaMinima)) {
            throw new IllegalArgumentException("El usuario debe tener al menos 16 años para registrarse.");
        }

        // Validar contraseña segura
        String password = dto.getPassword();
        if (password == null || !esPasswordSegura(password)) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales.");
        }

        // Validar tipo de documento y número de documento (DNI)
        if (!"DNI".equalsIgnoreCase(dto.getTipoDocumento())) {
            throw new IllegalArgumentException("Solo se permite el tipo de documento DNI.");
        }
        String numeroDocumento = dto.getNumeroDocumento();
        if (numeroDocumento == null || !numeroDocumento.matches("\\d{8}")) {
            throw new IllegalArgumentException("El número de documento debe tener 8 dígitos numéricos.");
        }

        // Validar teléfono usando ValidPhoneNumberValidator lógica
        if (dto.getTelefono() != null && !dto.getTelefono().trim().isEmpty()) {
            ValidPhoneNumberValidator validator = new ValidPhoneNumberValidator();
            if (!validator.isValid(dto.getTelefono(), null)) {
                throw new IllegalArgumentException("El número de teléfono no es válido.");
            }
        }

        String nombreProvincia = "Desconocido";
        if (dto.getProvinciaId() != null) {
            try {
                nombreProvincia = provinciaRepositorio.findById(dto.getProvinciaId())
                        .map(Provincia::getNombre)
                        .orElse("Desconocido");
                logger.info("Provincia encontrada: {}", nombreProvincia);
            } catch (Exception e) {
                logger.error("Error al obtener provincia: {}", e.getMessage());
                nombreProvincia = "Desconocido";
            }
        }

        // Ahora guardamos el usuario en la base de datos de seguridad
        guardarUsuarioEnBaseDatos(dto, nombreProvincia);
    }

    @Override
    public void actualizarPerfil(String usernameActual, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepositorio.findByUsername(usernameActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar si el nuevo username ya está en uso por otro usuario
        if (!usernameActual.equals(usuarioDTO.getUsername()) && existePorUsername(usuarioDTO.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        // Actualizar campos editables
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setNombres(usuarioDTO.getNombres());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setProvincia(usuarioDTO.getProvincia());
        usuario.setDireccion(usuarioDTO.getDireccion());

        usuarioRepositorio.save(usuario);
    }

    private boolean esPasswordSegura(String password) {
        if (password.length() < 8) {
            return false;
        }
        Pattern mayuscula = Pattern.compile("[A-Z]");
        Pattern minuscula = Pattern.compile("[a-z]");
        Pattern numero = Pattern.compile("[0-9]");
        Pattern especial = Pattern.compile("[^A-Za-z0-9]");
        return mayuscula.matcher(password).find() &&
                minuscula.matcher(password).find() &&
                numero.matcher(password).find() &&
                especial.matcher(password).find();
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
            logger.info("Rol USUARIO asignado");

            logger.info("Guardando usuario en base de datos...");
            Usuario guardado = usuarioRepositorio.save(nuevo);
            logger.info("Usuario guardado exitosamente con ID: {}", guardado.getId());

        } catch (Exception e) {
            logger.error("Error al guardar usuario: {}", e.getMessage(), e);
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

    private UsuarioPerfilDTO construirPerfilDTO(Usuario usuario, int pubPage, int evtPage) {
        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setRoles(usuario.getRoles().stream().map(Rol::getNombre).toList());

        PageRequest pubPageable = PageRequest.of(pubPage, 10);
        PageRequest evtPageable = PageRequest.of(evtPage, 10);

        Page<Publicacion> publicacionesPage = publicacionRepositorio.findByAutor(usuario.getId(), pubPageable);
        Page<Comentario> comentariosPage = comentarioRepositorio.findByUsuarioId(usuario.getId(), pubPageable);
        Page<UsuarioEvento> usuarioEventosPage = usuarioEventoRepositorio.findByIdUsuarioId(usuario.getId(),
                evtPageable);

        // Extraer IDs de eventos
        var eventoIds = usuarioEventosPage.stream()
                .map(ue -> ue.getId().getEventoId())
                .toList();

        // Obtener eventos por IDs
        List<Evento> eventosList = eventoIds.isEmpty()
                ? Collections.emptyList()
                : new ArrayList<>(eventoRepositorio.findAllById(eventoIds));

        // Convertir lista a Page (sin paginación real, solo para compatibilidad)
        Page<Evento> eventosPage = new PageImpl<>(eventosList, evtPageable, usuarioEventosPage.getTotalElements());

        dto.setPublicaciones(publicacionesPage);
        dto.setComentarios(comentariosPage);
        dto.setEventos(eventosPage);

        dto.setTotalPublicaciones((int) publicacionesPage.getTotalElements());
        dto.setTotalComentarios((int) comentariosPage.getTotalElements());
        dto.setTotalEventosAsistidos((int) usuarioEventosPage.getTotalElements());

        return dto;
    }

    @Override
    public Optional<Usuario> obtenerEntidadPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username);
    }

}
