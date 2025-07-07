package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "configuracion")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuracion {

    @Id
    @Column(length = 100)
    private String clave;

    @NotBlank
    @Column(nullable = false)
    private String valor;
}
