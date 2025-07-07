package com.piuraexpressa.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDocumentNumberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocumentNumber {
    String message() default "El número de documento no es válido para el tipo seleccionado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String documentNumberField() default "numeroDocumento";
    String documentTypeField() default "tipoDocumento";
}
