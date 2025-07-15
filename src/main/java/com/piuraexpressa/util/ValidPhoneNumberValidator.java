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
            return true;
        }

        // Limpiar el número de teléfono
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");

        //(9 dígitos empezando con 9)
        if (cleanedNumber.matches("^9\\d{8}$")) {
            return true;
        }

        //teléfonos fijos de Lima (7 dígitos)
        if (cleanedNumber.matches("^[1-7]\\d{6}$")) {
            return true;
        }

        //+51
        if (cleanedNumber.matches("^51[1-9]\\d{6,8}$")) {
            return true;
        }

        return false;
    }
}
