package com.piuraexpressa.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class EventoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String provincia;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String ubicacion;
    private Integer capacidad;
    private BigDecimal precio;
    private String estado; // "proximo", "en_curso", "finalizado"
    private String imagenUrl;
    private Boolean activo;

    private Long usuarioId;
    private String organizador;

    private Double porcentajeOcupado;
    private Double promedioCalificacion = 0.0;
    private Integer cantidadResenas;
    private Integer participantesActuales;

    // Estados para control de botones
    private Boolean puedeParticipar;
    private Boolean yaParticipa;
    private Boolean finalizado;
    private Boolean yaResenado;
    
    private List<ResenaDTO> resenas;

    // Texto bonito
    public String getRangoFechas() {
        if (fechaInicio != null && fechaFin != null) {
            String inicio = formatFechaBonita(fechaInicio);
            String fin = formatFechaBonita(fechaFin);
            return inicio + " - " + fin;
        }
        return "Sin definir";
    }

    private String formatFechaBonita(LocalDateTime fecha) {
        String dia = String.format("%02d", fecha.getDayOfMonth());
        String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String año = String.valueOf(fecha.getYear());
        return dia + " " + mes + " " + año;
    }

    public String getPrecioTexto() {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) == 0) {
            return "Gratis";
        }
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
        return formato.format(precio);
    }

    public String getEstadoCss() {
        if (fechaFin != null && fechaFin.isBefore(LocalDateTime.now())) {
            return "estado-finalizado";
        } else if (fechaInicio != null && fechaInicio.isBefore(LocalDateTime.now())) {
            return "estado-en-curso";
        }
        return "estado-proximo";
    }

    public String getDescripcionCorta() {
        return descripcion != null && descripcion.length() > 150
                ? descripcion.substring(0, 150) + "..."
                : descripcion;
    }

    public String getPromedioCalificacionTexto() {
        if (promedioCalificacion == null)
            return "Sin reseñas";
        return String.format("%.1f (%d reseñas)", promedioCalificacion, cantidadResenas != null ? cantidadResenas : 0);
    }
}
