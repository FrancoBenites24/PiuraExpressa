// package com.piuraexpressa.config;

// import com.piuraexpressa.model.seguridad.Permiso;
// import com.piuraexpressa.model.seguridad.Rol;
// import com.piuraexpressa.servicio.PermisoServicio;
// import com.piuraexpressa.servicio.RolServicio;
// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Component;

// import java.util.Arrays;
// import java.util.HashSet;
// import java.util.List;

// @Component
// @RequiredArgsConstructor
// public class DataInitializer {

//     private final PermisoServicio permisoServicio;
//     private final RolServicio rolServicio;

//     @PostConstruct
//     public void init() {
//         // Definir permisos usados en los controladores
//         List<Permiso> permisos = Arrays.asList(
//                 Permiso.builder().nombre("ADMINISTRAR_USUARIOS").recurso("usuario").accion("administrar").activo(true).build(),
//                 Permiso.builder().nombre("MOSTRAR_DETALLE_USUARIOS").recurso("usuario").accion("mostrar_detalle").activo(true).build(),
//                 Permiso.builder().nombre("CAMBIAR_ROL_USUARIO").recurso("usuario").accion("cambiar_rol").activo(true).build(),
//                 Permiso.builder().nombre("DESACTIVAR_USUARIO").recurso("usuario").accion("desactivar").activo(true).build(),
//                 Permiso.builder().nombre("ACTIVAR_USUARIO").recurso("usuario").accion("activar").activo(true).build(),
//                 Permiso.builder().nombre("MOSTRAR_DASHBOARD_ADMIN").recurso("dashboard").accion("mostrar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_ESTADISTICAS_PROVINCIA").recurso("estadisticas_provincia").accion("administrar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_LISTAR").recurso("historia_provincia").accion("listar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_NUEVO").recurso("historia_provincia").accion("nuevo").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_GUARDAR").recurso("historia_provincia").accion("guardar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_EDITAR").recurso("historia_provincia").accion("editar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_ACTUALIZAR").recurso("historia_provincia").accion("actualizar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_HISTORIA_PROVINCIA_ELIMINAR").recurso("historia_provincia").accion("eliminar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_LISTAR").recurso("provincia").accion("listar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_NUEVO").recurso("provincia").accion("nuevo").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_GUARDAR").recurso("provincia").accion("guardar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_EDITAR").recurso("provincia").accion("editar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_ACTUALIZAR").recurso("provincia").accion("actualizar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_ACTIVAR").recurso("provincia").accion("activar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_DESACTIVAR").recurso("provincia").accion("desactivar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_ELIMINAR").recurso("provincia").accion("eliminar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PROVINCIA_GESTION").recurso("provincia").accion("gestion").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES").recurso("punto_interes").accion("administrar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_LISTAR").recurso("punto_interes").accion("listar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_NUEVO").recurso("punto_interes").accion("nuevo").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_GUARDAR").recurso("punto_interes").accion("guardar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_EDITAR").recurso("punto_interes").accion("editar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_ELIMINAR").recurso("punto_interes").accion("eliminar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_DESACTIVAR").recurso("punto_interes").accion("desactivar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_PUNTO_INTERES_ACTIVAR").recurso("punto_interes").accion("activar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO").recurso("evento").accion("administrar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_LISTAR").recurso("evento").accion("listar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_ACTIVAR").recurso("evento").accion("activar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_DESACTIVAR").recurso("evento").accion("desactivar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_ELIMINAR").recurso("evento").accion("eliminar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_NUEVO").recurso("evento").accion("nuevo").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_CREAR").recurso("evento").accion("crear").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_EDITAR").recurso("evento").accion("editar").activo(true).build(),
//                 Permiso.builder().nombre("ADMINISTRAR_EVENTO_ACTUALIZAR").recurso("evento").accion("actualizar").activo(true).build(),
//                 Permiso.builder().nombre("USUARIO_VER_PERFIL").recurso("perfil").accion("ver").activo(true).build(),
//                 Permiso.builder().nombre("USUARIO_ACTUALIZAR_PERFIL").recurso("perfil").accion("actualizar").activo(true).build(),
//                 Permiso.builder().nombre("USUARIO_LISTAR_COMENTARIOS").recurso("comentario").accion("listar").activo(true).build(),
//                 Permiso.builder().nombre("USUARIO_CREAR_COMENTARIO").recurso("comentario").accion("crear").activo(true).build()
//         );

//         // Guardar permisos si no existen
//         permisos.forEach(permisoServicio::crearSiNoExiste);

// // Recuperar los roles existentes antes de asignarles permisos
// Rol adminExistente = rolServicio.buscarPorNombre("ADMINISTRADOR")
//         .orElseThrow(() -> new RuntimeException("El rol ADMINISTRADOR no fue encontrado"));

// adminExistente.setPermisos(new HashSet<>(permisos));
// rolServicio.guardar(adminExistente);

// Rol usuarioExistente = rolServicio.buscarPorNombre("USUARIO")
//         .orElseThrow(() -> new RuntimeException("El rol USUARIO no fue encontrado"));

// usuarioExistente.setPermisos(new HashSet<>());
// rolServicio.guardar(usuarioExistente);

// }
// }