package com.piuraexpressa.model.dominio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "categoria_noticia", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nombre")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaNoticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nombre;

    @Column
    private String descripcion;
}