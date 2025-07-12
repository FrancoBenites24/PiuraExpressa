package com.piuraexpressa.model.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEvento {

    @EmbeddedId
    private UsuarioEventoId id;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "confirmado")
    private boolean confirmado;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;
}
