package com.piuraexpressa.specification;

import com.piuraexpressa.model.dominio.Publicacion;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PublicacionSpecification {

    public static Specification<Publicacion> filtrarPorUsuario(Long usuarioId) {
        return (root, query, criteriaBuilder) -> {
            if (usuarioId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("autor"), usuarioId);
        };
    }

    public static Specification<Publicacion> filtrarPorFechaDesde(LocalDateTime fechaDesde) {
        return (root, query, criteriaBuilder) -> {
            if (fechaDesde == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("fechaCreacion"), fechaDesde);
        };
    }

    // Para ordenar por suma de likes y comentarios, se debe hacer en el servicio con join y group by,
    // pero aquí solo se filtra. Ordenamiento se hará en el servicio.
}
