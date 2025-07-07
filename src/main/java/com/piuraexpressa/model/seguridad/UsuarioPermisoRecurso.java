package com.piuraexpressa.model.seguridad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_permiso_recurso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "usuarioId", "permisoId", "recursoId" })
public class UsuarioPermisoRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "permiso_id", nullable = false)
    private Long permisoId;

    @Column(name = "recurso_id", nullable = false)
    private Long recursoId;

    @Column(name = "recurso_tipo", nullable = false, length = 50)
    private String recursoTipo;
}
