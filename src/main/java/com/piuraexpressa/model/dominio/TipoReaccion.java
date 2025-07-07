package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tipo_reaccion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TipoReaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String nombre;

    private String icono;

    private String descripcion;
}
