package com.piuraexpressa.dto;

import java.time.LocalDate;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
// @ValidDocumentNumber(documentNumberField = "numeroDocumento", documentTypeField = "tipoDocumento")
public class RegistroDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmarPassword;

    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;

    @NotBlank
    private String tipoDocumento;

    @NotBlank
    private String numeroDocumento;

    private String telefono;
    private LocalDate fechaNacimiento;
    private Long provinciaId;
    private String distrito;
    private String direccion;
}
