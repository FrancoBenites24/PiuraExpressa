package com.piuraexpressa.model.seguridad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.piuraexpressa.model.dominio.TipoDocumento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "numero_documento")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @NotBlank
    @Size(max = 20)
    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;

    @Size(max = 255)
    @Column(name = "direccion")
    private String direccion;

    @Size(max = 100)
    @Column(name = "distrito")
    private String distrito;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "provincia")
    private String provincia;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "motivo_baja")
    private String motivoBaja;

    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime actualizadoEn;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rol_usuario", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
   
}
