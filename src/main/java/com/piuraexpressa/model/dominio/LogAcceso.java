package com.piuraexpressa.model.dominio;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "log_acceso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Lob
    private String userAgent;

    @NotBlank
    @Column(name = "tipo_acceso", nullable = false, length = 50)
    private String tipoAcceso;

    private String detalles;

    @Builder.Default
    @Column(name = "fecha_acceso")
    private LocalDateTime fechaAcceso = LocalDateTime.now();
}
