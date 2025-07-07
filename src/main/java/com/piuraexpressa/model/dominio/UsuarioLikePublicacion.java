package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_like_publicacion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioLikePublicacion {

    @EmbeddedId
    private UsuarioLikePublicacionId id;

    @Embeddable
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class UsuarioLikePublicacionId implements java.io.Serializable {

        @Column(name = "usuario_id")
        private Integer usuarioId;

        @Column(name = "publicacion_id")
        private Integer publicacionId;

        @Column(name = "tipo_reaccion_id")
        private Integer tipoReaccionId;
    }
}
