package com.piuraexpressa.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

import com.piuraexpressa.model.dominio.TipoDocumento;

public class ValidDocumentNumberValidator implements ConstraintValidator<ValidDocumentNumber, Object> {

    private String documentNumberField;
    private String documentTypeField;

    @Override
    public void initialize(ValidDocumentNumber constraintAnnotation) {
        this.documentNumberField = constraintAnnotation.documentNumberField();
        this.documentTypeField = constraintAnnotation.documentTypeField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        try {
            Field documentNumberFieldObj = obj.getClass().getDeclaredField(documentNumberField);
            Field documentTypeFieldObj = obj.getClass().getDeclaredField(documentTypeField);
            
            documentNumberFieldObj.setAccessible(true);
            documentTypeFieldObj.setAccessible(true);
            
            String documentNumber = (String) documentNumberFieldObj.get(obj);
            TipoDocumento documentType = (TipoDocumento) documentTypeFieldObj.get(obj);

            if (documentNumber == null || documentType == null) {
                return true; // Permitir valores nulos, usar @NotNull para requerir
            }

            return isValidDocumentNumber(documentNumber, documentType, context);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidDocumentNumber(String documentNumber, TipoDocumento documentType, 
                                        ConstraintValidatorContext context) {
        
        String cleanedNumber = documentNumber.replaceAll("[\\s\\-\\.]", "");
        
        switch (documentType) {
            case DNI:
                if (!isValidDNI(cleanedNumber)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{validator.documentNumber.dni}")
                           .addConstraintViolation();
                    return false;
                }
                break;
                
            case CARNET_EXTRANJERIA:
                if (!isValidCarnetExtranjeria(cleanedNumber)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{validator.documentNumber.ce}")
                           .addConstraintViolation();
                    return false;
                }
                break;
                
            case PASAPORTE:
                if (!isValidPasaporte(cleanedNumber)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{validator.documentNumber.passport}")
                           .addConstraintViolation();
                    return false;
                }
                break;
                
            default:
                return false;
        }
        
        return true;
    }

    private boolean isValidDNI(String dni) {
        // DNI peruano: 8 dígitos
        if (!dni.matches("^\\d{8}$")) {
            return false;
        }
        
        // Validación adicional: no puede ser todos ceros o números consecutivos
        if (dni.equals("00000000") || dni.equals("12345678") || dni.equals("87654321")) {
            return false;
        }
        
        return true;
    }

    private boolean isValidCarnetExtranjeria(String ce) {
        // Carnet de Extranjería: 9 dígitos o formato específico
        return ce.matches("^\\d{9}$") || ce.matches("^[A-Z]\\d{8}$");
    }

    private boolean isValidPasaporte(String passport) {
        // Pasaporte: formato alfanumérico, entre 6 y 12 caracteres
        return passport.matches("^[A-Z0-9]{6,12}$");
    }
}
