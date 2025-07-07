package com.piuraexpressa.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.piuraexpressa.model.dominio.TipoDocumento;

@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no debe exceder los 100 caracteres")
    private String apellido;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario no debe exceder los 50 caracteres")
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Size(max = 150, message = "El correo no debe exceder los 150 caracteres")
    private String email;

    @Size(max = 20, message = "El número de documento no debe exceder los 20 caracteres")
    private String numeroDocumento;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20, message = "El tipo de documento no debe exceder los 20 caracteres")
    private TipoDocumento tipoDocumento;

    @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres")
    private String telefono;

    private String provincia;
    
    @NotBlank(message = "El rol principal es obligatorio")
    private String rolPrincipal; // podría ser ID o nombre de rol, según lo necesites

    private List<Long> rolesSecundarios; // ids de roles secundarios

    private boolean activo = true;

    private LocalDate fechaBaja;
    private String motivoBaja;
    private Long provinciaId;

}
