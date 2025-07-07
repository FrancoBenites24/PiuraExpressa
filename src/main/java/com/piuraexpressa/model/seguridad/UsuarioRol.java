package com.piuraexpressa.model.seguridad;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "rol_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRol {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false,
        foreignKey = @ForeignKey(name = "usuario_roles_usuario_id_fkey"))
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rolId")
    @JoinColumn(name = "rol_id", nullable = false,
        foreignKey = @ForeignKey(name = "usuario_roles_rol_id_fkey"))
    private Rol rol;

    @Builder.Default
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion = LocalDateTime.now();
}
