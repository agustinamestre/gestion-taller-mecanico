package com.taller.gestion_taller.infrastructure.rest.validation.telefono;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class {@link TelefonoValidoValidator}
 */
class TelefonoValidoValidatorTest {

    private static final String TELEFONO_VALIDO_10_DIGITOS = "3364249176";
    private static final String TELEFONO_VALIDO_CON_CODIGO_PAIS = "543364249176";
    private static TelefonoValidoValidator telefonoValidator;

    @BeforeAll
    static void setUp() {
        // TelefonoValidoValidator es stateless, así que puedo reutilizar la misma instancia en cada test
        telefonoValidator = new TelefonoValidoValidator();
    }

    @Test
    @DisplayName("Un teléfono de 10 dígitos que no empieza con 0 es válido")
    void telefono_de_10_digitos_es_valido() {
        var isTelefonoValid = telefonoValidator.isValid(TELEFONO_VALIDO_10_DIGITOS, mock(ConstraintValidatorContext.class));

        assertEquals(10, TELEFONO_VALIDO_10_DIGITOS.length());
        assertTrue(isTelefonoValid);
    }

    @Test
    @DisplayName("Un teléfono con código de país (12 dígitos) es válido")
    void telefono_con_codigo_de_pais_es_valido() {
        var isTelefonoValid = telefonoValidator.isValid(TELEFONO_VALIDO_CON_CODIGO_PAIS, mock(ConstraintValidatorContext.class));

        assertEquals(12, TELEFONO_VALIDO_CON_CODIGO_PAIS.length());
        assertTrue(isTelefonoValid);
    }

    @Test
    @DisplayName("Un teléfono de 11 dígitos es válido")
    void telefono_de_11_digitos_es_valido() {
        var isTelefonoValid = telefonoValidator.isValid("33642491760", mock(ConstraintValidatorContext.class));

        assertTrue(isTelefonoValid);
    }

    @Test
    @DisplayName("Un teléfono es inválido si es null")
    void telefono_es_invalido_si_es_null() {
        assertErrorCuandoTelefonoEs(null, "El telefono es obligatorio.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si es string vacío")
    void telefono_es_invalido_si_es_string_vacio() {
        assertErrorCuandoTelefonoEs("", "El telefono es obligatorio.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene solo espacios")
    void telefono_es_invalido_si_contiene_solo_espacios() {
        assertErrorCuandoTelefonoEs("   ", "El telefono es obligatorio.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene letras")
    void telefono_es_invalido_si_contiene_letras() {
        assertErrorCuandoTelefonoEs("336424abc", "El telefono solo debe contener numeros.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene guiones")
    void telefono_es_invalido_si_contiene_guiones() {
        assertErrorCuandoTelefonoEs("336-424-9176", "El telefono solo debe contener numeros.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene espacios")
    void telefono_es_invalido_si_contiene_espacios() {
        assertErrorCuandoTelefonoEs("336 424 9176", "El telefono solo debe contener numeros.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene paréntesis")
    void telefono_es_invalido_si_contiene_parentesis() {
        assertErrorCuandoTelefonoEs("(336)4249176", "El telefono solo debe contener numeros.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si contiene el símbolo +")
    void telefono_es_invalido_si_contiene_simbolo_mas() {
        assertErrorCuandoTelefonoEs("+543364249176", "El telefono solo debe contener numeros.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si empieza con 0")
    void telefono_es_invalido_si_empieza_con_cero() {
        assertErrorCuandoTelefonoEs("0336424917", "El numero de telefono no debe comenzar con 0.");
    }

    @Test
    @DisplayName("Un teléfono es inválido si es solo ceros")
    void telefono_es_invalido_si_es_solo_ceros() {
        assertErrorCuandoTelefonoEs("0000000000", "El numero de telefono no debe comenzar con 0.");
    }

    // ==================== HELPER METHOD ====================

    private void assertErrorCuandoTelefonoEs(String telefono, String message) {
        var contextMock = mock(ConstraintValidatorContext.class);
        var violationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(contextMock.buildConstraintViolationWithTemplate(message))
                .thenReturn(violationBuilderMock);

        var isTelefonoValid = telefonoValidator.isValid(telefono, contextMock);

        assertFalse(isTelefonoValid);
        verify(contextMock, times(1)).disableDefaultConstraintViolation();
        verify(contextMock, times(1)).buildConstraintViolationWithTemplate(message);
        verify(violationBuilderMock, times(1)).addConstraintViolation();
    }
}
