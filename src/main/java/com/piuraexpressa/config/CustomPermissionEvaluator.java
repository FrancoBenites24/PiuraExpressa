package com.piuraexpressa.config;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        // Primero, verifica si el usuario tiene una autoridad que coincida directamente con el permiso.
        // Esto mantiene la compatibilidad con permisos generales como "ACCESS".
        String directPermission = permission.toString().toUpperCase();
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(directPermission))) {
            return true;
        }

        // Si el objeto de destino es nulo, no podemos continuar con la verificación basada en la entidad.
        if (targetDomainObject == null) {
            return false;
        }

        // --- Verificación de permisos basada en la entidad (ej. ADMIN_ROLES_ACCESS) ---
        String target = targetDomainObject.toString();
        String action = permission.toString().toUpperCase();

        // Extrae la entidad de la ruta URL (ej. de "/admin/roles/editar" extrae "roles")
        String entity = extractEntityFromPath(target);
        if (entity.isEmpty()) {
            // Si no se puede extraer la entidad, intenta verificar el permiso directamente
            return authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals(directPermission));
        }

        // Construye el nombre del permiso esperado según la convención en DataInitializer
        String expectedPermission = "ADMIN_" + entity.toUpperCase() + "_" + action;

        // Verifica si los permisos del usuario contienen el permiso específico requerido.
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(expectedPermission));
    }

    /**
     * Extrae el nombre de la entidad de una ruta URL que sigue el patrón "/admin/{entidad}/...".
     * @param path La ruta URL.
     * @return El nombre de la entidad en minúsculas, o una cadena vacía si no se encuentra.
     */
    private String extractEntityFromPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "";
        }
        // Divide la ruta por "/" y busca la parte que sigue a "admin".
        List<String> parts = Arrays.asList(path.split("/"));
        int adminIndex = parts.indexOf("admin");
        if (adminIndex != -1 && parts.size() > adminIndex + 1) {
            // Retorna la parte justo después de "admin", ej. "roles", "usuarios".
            return parts.get(adminIndex + 1);
        }
        return "";
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Este método es ideal para la seguridad a nivel de objeto, pero no se utiliza actualmente.
        // Por ejemplo: hasPermission(1, 'Post', 'edit')
        return false;
    }
}
