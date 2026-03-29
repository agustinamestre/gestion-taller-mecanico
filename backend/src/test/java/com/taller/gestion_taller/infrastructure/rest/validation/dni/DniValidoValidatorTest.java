package com.taller.gestion_taller.infrastructure.rest.validation.dni;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class {@link DniValidoValidator}
 */
class DniValidoValidatorTest {

    private static final String DNI_VALIDO_8_DIGITOS = "12345678";
    private static final String DNI_VALIDO_7_DIGITOS = "1234567";
    private static DniValidoValidator dniValidator;

    @BeforeAll
    static void setUp() {
        // DniValidoValidator es stateless, así que puedo reutilizar la misma instancia en cada test
        dniValidator = new DniValidoValidator();
    }


    @Test
    @DisplayName("Un DNI de 8 dígitos numéricos que no empieza con 0 es válido")
    void dni_de_8_digitos_es_valido() {
        var isDniValid = dniValidator.isValid(DNI_VALIDO_8_DIGITOS, mock(ConstraintValidatorContext.class));

        assertEquals(8, DNI_VALIDO_8_DIGITOS.length());
        assertTrue(isDniValid);
    }

    @Test
    @DisplayName("Un DNI de 7 dígitos numéricos que no empieza con 0 es válido")
    void dni_de_7_digitos_es_valido() {
        var isDniValid = dniValidator.isValid(DNI_VALIDO_7_DIGITOS, mock(ConstraintValidatorContext.class));

        assertEquals(7, DNI_VALIDO_7_DIGITOS.length());
        assertTrue(isDniValid);
    }

    @Test
    @DisplayName("Un DNI es inválido si es null")
    void dni_es_invalido_si_es_null() {
        assertErrorCuandoDniEs(null, "El numero de DNI es obligatorio.");
    }

    @Test
    @DisplayName("Un DNI es inválido si es string vacío")
    void dni_es_invalido_si_es_string_vacio() {
        assertErrorCuandoDniEs("", "El numero de DNI es obligatorio.");
    }

    @Test
    @DisplayName("Un DNI es inválido si contiene solo espacios")
    void dni_es_invalido_si_contiene_solo_espacios() {
        assertErrorCuandoDniEs("   ", "El numero de DNI es obligatorio.");
    }

    @Test
    @DisplayName("Un DNI es inválido si contiene letras")
    void dni_es_invalido_si_contiene_letras() {
        assertErrorCuandoDniEs("1234567A", "Numero de DNI invalido.");
    }

    @Test
    @DisplayName("Un DNI es inválido si contiene caracteres especiales")
    void dni_es_invalido_si_contiene_caracteres_especiales() {
        assertErrorCuandoDniEs("1234-567", "Numero de DNI invalido.");
    }

    @Test
    @DisplayName("Un DNI es inválido si contiene puntos")
    void dni_es_invalido_si_contiene_puntos() {
        assertErrorCuandoDniEs("12.345.678", "Numero de DNI invalido.");
    }

    @Test
    @DisplayName("Un DNI es inválido si contiene espacios entre números")
    void dni_es_invalido_si_contiene_espacios_entre_numeros() {
        assertErrorCuandoDniEs("1234 5678", "Numero de DNI invalido.");
    }

    @Test
    @DisplayName("Un DNI es inválido si tiene menos de 7 dígitos")
    void dni_es_invalido_si_tiene_menos_de_7_digitos() {
        assertErrorCuandoDniEs("123456", "El numero de DNI debe tener entre 7 u 8 digitos.");
    }

    @Test
    @DisplayName("Un DNI es inválido si tiene más de 8 dígitos")
    void dni_es_invalido_si_tiene_mas_de_8_digitos() {
        assertErrorCuandoDniEs("123456789", "El numero de DNI debe tener entre 7 u 8 digitos.");
    }

    @Test
    @DisplayName("Un DNI es inválido si tiene solo 1 dígito")
    void dni_es_invalido_si_tiene_solo_1_digito() {
        assertErrorCuandoDniEs("1", "El numero de DNI debe tener entre 7 u 8 digitos.");
    }

    @Test
    @DisplayName("Un DNI es inválido si empieza con 0")
    void dni_es_invalido_si_empieza_con_cero() {
        assertErrorCuandoDniEs("01234567", "El numero de DNI no puede empezar con 0.");
    }

    @Test
    @DisplayName("Un DNI de 7 dígitos es inválido si empieza con 0")
    void dni_de_7_digitos_es_invalido_si_empieza_con_cero() {
        assertErrorCuandoDniEs("0123456", "El numero de DNI no puede empezar con 0.");
    }

    // ==================== HELPER METHOD ====================

    private void assertErrorCuandoDniEs(String dni, String message) {
        var contextMock = mock(ConstraintValidatorContext.class);
        var violationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(contextMock.buildConstraintViolationWithTemplate(message))
                .thenReturn(violationBuilderMock);

        var isDniValid = dniValidator.isValid(dni, contextMock);

        assertFalse(isDniValid);
        verify(contextMock, times(1)).disableDefaultConstraintViolation();
        verify(contextMock, times(1)).buildConstraintViolationWithTemplate(message);
        verify(violationBuilderMock, times(1)).addConstraintViolation();
    }
}
