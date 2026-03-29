package com.taller.gestion_taller.infrastructure.rest.validation.email;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class {@link EmailValidator}
 */
class EmailValidatorTest {

    private static final String EMAIL_VALIDO = "juan.perez@gmail.com";
    private static EmailValidator emailValidator;

    @BeforeAll
    static void setUp() {
        // EmailValidator es stateless, así que puedo reutilizar la misma instancia en cada test
        emailValidator = new EmailValidator();
    }

    @Test
    @DisplayName("Un email con formato correcto es válido")
    void email_con_formato_correcto_es_valido() {
        var isEmailValid = emailValidator.isValid(EMAIL_VALIDO, mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email con subdominio es válido")
    void email_con_subdominio_es_valido() {
        var isEmailValid = emailValidator.isValid("juan@mail.empresa.com", mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email con guiones y puntos es válido")
    void email_con_guiones_y_puntos_es_valido() {
        var isEmailValid = emailValidator.isValid("juan.perez-trabajo@gmail.com", mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email null es válido (campo opcional)")
    void email_null_es_valido_campo_opcional() {
        var isEmailValid = emailValidator.isValid(null, mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email vacío es válido (campo opcional)")
    void email_vacio_es_valido_campo_opcional() {
        var isEmailValid = emailValidator.isValid("", mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email con solo espacios es válido (campo opcional)")
    void email_con_solo_espacios_es_valido_campo_opcional() {
        var isEmailValid = emailValidator.isValid("   ", mock(ConstraintValidatorContext.class));

        assertTrue(isEmailValid);
    }

    @Test
    @DisplayName("Un email sin @ es inválido")
    void email_sin_arroba_es_invalido() {
        var isEmailValid = emailValidator.isValid("juangmail.com", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }

    @Test
    @DisplayName("Un email sin dominio es inválido")
    void email_sin_dominio_es_invalido() {
        var isEmailValid = emailValidator.isValid("juan@", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }

    @Test
    @DisplayName("Un email sin extensión es inválido")
    void email_sin_extension_es_invalido() {
        var isEmailValid = emailValidator.isValid("juan@gmail", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }

    @Test
    @DisplayName("Un email sin usuario es inválido")
    void email_sin_usuario_es_invalido() {
        var isEmailValid = emailValidator.isValid("@gmail.com", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }

    @Test
    @DisplayName("Un email con espacios es inválido")
    void email_con_espacios_es_invalido() {
        var isEmailValid = emailValidator.isValid("juan perez@gmail.com", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }

    @Test
    @DisplayName("Un email con múltiples @ es inválido")
    void email_con_multiples_arroba_es_invalido() {
        var isEmailValid = emailValidator.isValid("juan@@gmail.com", mock(ConstraintValidatorContext.class));

        assertFalse(isEmailValid);
    }
}
