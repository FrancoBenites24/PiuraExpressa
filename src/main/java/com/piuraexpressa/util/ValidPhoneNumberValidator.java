package com.piuraexpressa.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return true; // Permitir valores nulos/vacíos, usar @NotBlank para requerir
        }

        // Limpiar el número de teléfono (remover espacios, guiones, paréntesis)
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");

        // Validar números de teléfono peruanos
        // Celulares: 9 dígitos que empiecen con 9
        // Fijos Lima: 7 dígitos que empiecen con números específicos
        // Con código de país: +51 seguido del número

        // Patrón para celulares peruanos (9 dígitos empezando con 9)
        if (cleanedNumber.matches("^9\\d{8}$")) {
            return true;
        }

        // Patrón para teléfonos fijos de Lima (7 dígitos)
        if (cleanedNumber.matches("^[1-7]\\d{6}$")) {
            return true;
        }

        // Patrón con código de país +51
        if (cleanedNumber.matches("^51[1-9]\\d{6,8}$")) {
            return true;
        }

        // Patrón internacional más flexible
        if (cleanedNumber.matches("^\\+?[1-9]\\d{7,14}$")) {
            return true;
        }

        return false;
    }
}
