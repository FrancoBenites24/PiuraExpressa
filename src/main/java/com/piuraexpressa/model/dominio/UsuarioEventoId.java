package com.piuraexpressa.model.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UsuarioEventoId implements Serializable {

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "evento_id")
    private Long eventoId;
}
