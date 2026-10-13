package com.taller.gestion_taller.infrastructure.rest.exception.advice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    enum Color { ROJO, AZUL }

    record Pedido(Color color, Integer cantidad) {}

    @RestController
    static class ControllerDePrueba {

        @PostMapping("/pedidos")
        Pedido crear(@RequestBody Pedido pedido) {
            return pedido;
        }

        @GetMapping("/pedidos")
        String listar(@RequestParam Color color) {
            return color.name();
        }

        @GetMapping("/duplicado")
        String duplicado() {
            throw new DataIntegrityViolationException("duplicate key value violates unique constraint \"uk_dni\"");
        }

        @GetMapping("/explota")
        String explota() {
            throw new IllegalStateException("detalle interno secreto");
        }
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ControllerDePrueba())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("JSON mal formado devuelve 400")
    void jsonMalFormado() throws Exception {
        mockMvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"color\": "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].errorCode").value("JSON_MAL_FORMADO"));
    }

    @Test
    @DisplayName("Enum invalido en el body devuelve 400 con los valores permitidos")
    void enumInvalidoEnBody() throws Exception {
        mockMvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"color\": \"VERDE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].errorCode").value("color_invalido"))
                .andExpect(jsonPath("$.error[0].errorMessage").value(containsString("ROJO, AZUL")));
    }

    @Test
    @DisplayName("Tipo invalido en el body devuelve 400")
    void tipoInvalidoEnBody() throws Exception {
        mockMvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content("{\"cantidad\": \"muchos\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].errorCode").value("cantidad_invalido"));
    }

    @Test
    @DisplayName("Enum invalido en un query param devuelve 400")
    void enumInvalidoEnQueryParam() throws Exception {
        mockMvc.perform(get("/pedidos").param("color", "VERDE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].errorCode").value("color_invalido"));
    }

    @Test
    @DisplayName("Query param requerido faltante devuelve 400")
    void queryParamFaltante() throws Exception {
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].errorCode").value("color_requerido"));
    }

    @Test
    @DisplayName("Clave duplicada devuelve 409 sin exponer el SQL")
    void claveDuplicada() throws Exception {
        mockMvc.perform(get("/duplicado"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error[0].errorCode").value("DATO_DUPLICADO"))
                .andExpect(content().string(not(containsString("uk_dni"))));
    }

    @Test
    @DisplayName("Metodo no soportado devuelve 405")
    void metodoNoSoportado() throws Exception {
        mockMvc.perform(post("/duplicado"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Error inesperado devuelve 500 con mensaje generico")
    void errorInesperado() throws Exception {
        mockMvc.perform(get("/explota"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error[0].errorCode").value("ERROR_TECNICO"))
                .andExpect(content().string(not(containsString("detalle interno"))))
                .andExpect(content().string(not(containsString("IllegalStateException"))));
    }
}
