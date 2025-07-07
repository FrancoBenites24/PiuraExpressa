package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_evento")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioEvento {

    @EmbeddedId
    private UsuarioEventoId id;

    @Embeddable
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class UsuarioEventoId implements java.io.Serializable {

        @Column(name = "usuario_id")
        private Integer usuarioId;

        @Column(name = "evento_id")
        private Integer eventoId;
    }
}
