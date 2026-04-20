package com.taller.gestion_taller.application.usecases.producto;

import com.taller.gestion_taller.domain.model.TipoProducto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObtenerTiposProductoUseCaseTest {

    private final ObtenerTiposProductoUseCase useCase = new ObtenerTiposProductoUseCase();

    @Test
    void debeRetornarTodosLosTiposDeProducto() {
        List<String> tipos = useCase.obtener();

        assertEquals(TipoProducto.values().length, tipos.size());
        assertTrue(tipos.contains("REPUESTO"));
        assertTrue(tipos.contains("INSUMO"));
        assertTrue(tipos.contains("SERVICIO"));
    }
}
