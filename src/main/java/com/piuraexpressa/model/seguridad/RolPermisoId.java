package com.piuraexpressa.model.seguridad;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RolPermisoId implements java.io.Serializable {
    private Long rolId;
    private Long permisoId;
}
