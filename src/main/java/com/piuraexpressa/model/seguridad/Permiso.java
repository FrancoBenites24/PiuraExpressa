package com.piuraexpressa.model.seguridad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permiso", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String recurso;

    @Column(length = 100, nullable = false)
    private String accion;

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_baja")
    private LocalDateTime fecha_baja;

    @Column(name = "motivo_baja")
    private String motivo_baja;

    // Relación inversa con roles
    @Builder.Default
    @ManyToMany(mappedBy = "permisos", fetch = FetchType.LAZY)
    private Set<Rol> roles = new HashSet<>();
}
