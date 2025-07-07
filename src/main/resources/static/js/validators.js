// Validadores personalizados
const validators = {
    // Validación de contraseña
    password: {
        validate: function(password) {
            const minLength = 8;
            const hasUpperCase = /[A-Z]/.test(password);
            const hasLowerCase = /[a-z]/.test(password);
            const hasNumbers = /\d/.test(password);
            const hasSpecialChars = /[!@#$%^&*(),.?":{}|<>]/.test(password);
            
            return {
                isValid: password.length >= minLength && 
                        hasUpperCase && 
                        hasLowerCase && 
                        hasNumbers && 
                        hasSpecialChars,
                errors: this.getPasswordErrors(password)
            };
        },
        
        getPasswordErrors: function(password) {
            const errors = [];
            
            if (password.length < 8) {
                errors.push('La contraseña debe tener al menos 8 caracteres');
            }
            if (!/[A-Z]/.test(password)) {
                errors.push('Debe incluir al menos una mayúscula');
            }
            if (!/[a-z]/.test(password)) {
                errors.push('Debe incluir al menos una minúscula');
            }
            if (!/\d/.test(password)) {
                errors.push('Debe incluir al menos un número');
            }
            if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
                errors.push('Debe incluir al menos un carácter especial');
            }
            
            return errors;
        },
        
        getStrength: function(password) {
            let strength = 0;
            
            // Longitud
            if (password.length >= 8) strength += 20;
            if (password.length >= 12) strength += 10;
            
            // Mayúsculas
            if (/[A-Z]/.test(password)) strength += 20;
            
            // Minúsculas
            if (/[a-z]/.test(password)) strength += 20;
            
            // Números
            if (/\d/.test(password)) strength += 20;
            
            // Símbolos
            if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength += 20;
            
            return {
                score: strength,
                label: this.getStrengthLabel(strength)
            };
        },
        
        getStrengthLabel: function(strength) {
            if (strength < 40) return 'Débil';
            if (strength < 80) return 'Media';
            return 'Fuerte';
        }
    },

    // Validación de documento
    document: {
        validate: function(number, type) {
            switch(type) {
                case 'DNI':
                    return this.validateDNI(number);
                case 'CARNET_EXTRANJERIA':
                    return this.validateCE(number);
                case 'PASAPORTE':
                    return this.validatePassport(number);
                default:
                    return { isValid: false, error: 'Tipo de documento no válido' };
            }
        },
        
        validateDNI: function(number) {
            const isValid = /^\d{8}$/.test(number);
            return {
                isValid,
                error: isValid ? null : 'El DNI debe tener 8 dígitos numéricos'
            };
        },
        
        validateCE: function(number) {
            const isValid = /^[A-Z]?\d{8,9}$/.test(number);
            return {
                isValid,
                error: isValid ? null : 'El Carnet de Extranjería debe tener 8-9 dígitos y puede empezar con una letra'
            };
        },
        
        validatePassport: function(number) {
            const isValid = /^[A-Z0-9]{6,12}$/.test(number);
            return {
                isValid,
                error: isValid ? null : 'El Pasaporte debe tener entre 6 y 12 caracteres alfanuméricos'
            };
        }
    },

    // Validación de teléfono
    phone: {
        validate: function(phone) {
            // Eliminar espacios y guiones
            phone = phone.replace(/[\s-]/g, '');
            
            // Validar formato peruano
            const isPeruvian = /^(\+51)?\d{9}$/.test(phone);
            
            // Validar formato internacional
            const isInternational = /^\+?[1-9]\d{6,14}$/.test(phone);
            
            return {
                isValid: isPeruvian || isInternational,
                error: this.getPhoneError(phone)
            };
        },
        
        getPhoneError: function(phone) {
            if (!phone) return 'El número de teléfono es requerido';
            if (!/^\+?[\d\s-]+$/.test(phone)) return 'El número solo puede contener dígitos, espacios y guiones';
            if (phone.length < 7) return 'El número es demasiado corto';
            if (phone.length > 15) return 'El número es demasiado largo';
            return null;
        },
        
        format: function(phone) {
            // Eliminar todo excepto dígitos
            phone = phone.replace(/\D/g, '');
            
            // Formato peruano
            if (phone.length === 9) {
                return phone.replace(/(\d{3})(\d{3})(\d{3})/, '$1 $2 $3');
            }
            
            // Formato internacional
            return phone.replace(/(\d{3})(\d{3})(\d{4,})/, '+$1 $2 $3');
        }
    },

    // Validación de email
    email: {
        validate: function(email) {
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const isValid = regex.test(email);
            
            return {
                isValid,
                error: isValid ? null : 'El formato del email no es válido'
            };
        }
    },

    // Validación de username
    username: {
        validate: function(username) {
            const minLength = 3;
            const maxLength = 50;
            const regex = /^[a-zA-Z0-9_]+$/;
            
            if (username.length < minLength) {
                return {
                    isValid: false,
                    error: `El nombre de usuario debe tener al menos ${minLength} caracteres`
                };
            }
            
            if (username.length > maxLength) {
                return {
                    isValid: false,
                    error: `El nombre de usuario no puede exceder ${maxLength} caracteres`
                };
            }
            
            if (!regex.test(username)) {
                return {
                    isValid: false,
                    error: 'El nombre de usuario solo puede contener letras, números y guiones bajos'
                };
            }
            
            return {
                isValid: true,
                error: null
            };
        }
    }
};

// Utilidades de formulario
const formUtils = {
    setFieldValidation: function(field, isValid, message = '') {
        const feedbackElement = field.nextElementSibling;
        
        if (isValid) {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
            if (feedbackElement) {
                feedbackElement.textContent = '';
            }
        } else {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
            if (feedbackElement) {
                feedbackElement.textContent = message;
            }
        }
    },
    
    updatePasswordStrength: function(strengthElement, score) {
        const progressBar = strengthElement.querySelector('.progress-bar');
        const strengthText = strengthElement.querySelector('.strength-text');
        
        progressBar.style.width = score + '%';
        
        if (score < 40) {
            progressBar.className = 'progress-bar bg-danger';
            strengthText.textContent = 'Débil';
        } else if (score < 80) {
            progressBar.className = 'progress-bar bg-warning';
            strengthText.textContent = 'Media';
        } else {
            progressBar.className = 'progress-bar bg-success';
            strengthText.textContent = 'Fuerte';
        }
    },
    
    formatPhoneNumber: function(input) {
        input.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 0) {
                if (value.length <= 9) {
                    // Formato nacional
                    value = value.replace(/(\d{3})(\d{3})(\d{3})/, '$1 $2 $3');
                } else {
                    // Formato internacional
                    value = '+' + value.replace(/(\d{2})(\d{3})(\d{3})(\d{3})/, '$1 $2 $3 $4');
                }
                e.target.value = value.trim();
            }
        });
    }
};

// Exportar utilidades
window.validators = validators;
window.formUtils = formUtils;
