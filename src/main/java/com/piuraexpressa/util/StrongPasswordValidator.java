package com.piuraexpressa.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*])(?=\\S+$).{8,}$";

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        // Validar longitud mínima
        if (password.length() < 8) {
            return false;
        }

        // Validar que contenga al menos un dígito
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Validar que contenga al menos una letra minúscula
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Validar que contenga al menos una letra mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Validar que contenga al menos un carácter especial
        if (!password.matches(".*[@#$%^&+=!*].*")) {
            return false;
        }

        // Validar que no contenga espacios en blanco
        if (password.matches(".*\\s.*")) {
            return false;
        }

        // Validar el patrón completo
        return password.matches(PASSWORD_PATTERN);
    }
}
