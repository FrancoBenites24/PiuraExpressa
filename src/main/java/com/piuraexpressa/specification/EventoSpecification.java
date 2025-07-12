package com.piuraexpressa.specification;

import com.piuraexpressa.dto.FiltroEventoDTO;
import com.piuraexpressa.model.dominio.Evento;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class EventoSpecification {

    public static Specification<Evento> filtrar(FiltroEventoDTO filtro) {
        return (Root<Evento> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicado = cb.conjunction();

            // texto en título o descripción
            if (filtro.getTexto() != null && !filtro.getTexto().isBlank()) {
                String texto = "%" + filtro.getTexto().toLowerCase() + "%";
                Predicate titulo = cb.like(cb.lower(root.get("titulo")), texto);
                Predicate descripcion = cb.like(cb.lower(root.get("descripcion")), texto);
                predicado = cb.and(predicado, cb.or(titulo, descripcion));
            }

            // provincia
            if (filtro.getProvincia() != null && !filtro.getProvincia().isBlank()) {
                predicado = cb.and(predicado,
                    cb.equal(cb.lower(root.get("provincia").get("nombre")), filtro.getProvincia().toLowerCase()));
            }

            // precio
            if (filtro.getPrecio() != null && !filtro.getPrecio().isBlank()) {
                switch (filtro.getPrecio()) {
                    case "gratuito" -> predicado = cb.and(predicado, cb.equal(root.get("precio"), BigDecimal.ZERO));
                    case "0-20" -> predicado = cb.and(predicado, cb.between(root.get("precio"), BigDecimal.ZERO, BigDecimal.valueOf(20)));
                    case "20-50" -> predicado = cb.and(predicado, cb.between(root.get("precio"), BigDecimal.valueOf(20), BigDecimal.valueOf(50)));
                    case "50-100" -> predicado = cb.and(predicado, cb.between(root.get("precio"), BigDecimal.valueOf(50), BigDecimal.valueOf(100)));
                    case "100+" -> predicado = cb.and(predicado, cb.greaterThan(root.get("precio"), BigDecimal.valueOf(100)));
                }
            }

            // fecha
            if (filtro.getFecha() != null && !filtro.getFecha().isBlank()) {
                LocalDateTime ahora = LocalDateTime.now();
                switch (filtro.getFecha()) {
                    case "hoy" -> {
                        LocalDate hoy = LocalDate.now();
                        predicado = cb.and(predicado,
                            cb.between(root.get("fechaInicio"),
                                hoy.atStartOfDay(), hoy.atTime(23, 59, 59)));
                    }
                    case "manana" -> {
                        LocalDate manana = LocalDate.now().plusDays(1);
                        predicado = cb.and(predicado,
                            cb.between(root.get("fechaInicio"),
                                manana.atStartOfDay(), manana.atTime(23, 59, 59)));
                    }
                    case "esta-semana" -> {
                        LocalDate start = LocalDate.now();
                        LocalDate end = start.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                        predicado = cb.and(predicado,
                            cb.between(root.get("fechaInicio"),
                                start.atStartOfDay(), end.atTime(23, 59, 59)));
                    }
                    case "este-mes" -> {
                        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
                        LocalDate finMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                        predicado = cb.and(predicado,
                            cb.between(root.get("fechaInicio"),
                                inicioMes.atStartOfDay(), finMes.atTime(23, 59, 59)));
                    }
                    case "proximos" -> {
                        predicado = cb.and(predicado, cb.greaterThanOrEqualTo(root.get("fechaInicio"), ahora));
                    }
                }
            }

            // solo eventos activos
            predicado = cb.and(predicado, cb.isTrue(root.get("activo")));

            return predicado;
        };
    }
}
