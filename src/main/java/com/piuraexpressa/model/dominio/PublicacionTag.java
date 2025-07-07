package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publicacion_tag")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PublicacionTag {

    @EmbeddedId
    private PublicacionTagId id;

    @Embeddable
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class PublicacionTagId implements java.io.Serializable {

        @Column(name = "publicacion_id")
        private Integer publicacionId;

        @Column(name = "tag_id")
        private Integer tagId;
    }
}
