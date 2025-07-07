package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_like_publicacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLikePublicacion {

    @EmbeddedId
    private UsuarioLikePublicacionId id;

    @Column(name = "fecha_like", nullable = false)
    private LocalDateTime fechaLike;

    @PrePersist
    public void prePersist() {
        fechaLike = LocalDateTime.now();
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioLikePublicacionId implements Serializable {
        @Column(name = "usuario_id")
        private Long usuarioId;

        @Column(name = "publicacion_id")
        private Long publicacionId;
    }
}
