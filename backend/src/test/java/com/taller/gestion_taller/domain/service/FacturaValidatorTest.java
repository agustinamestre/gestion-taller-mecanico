package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.ItemOrdenTrabajo;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.TipoFactura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("FacturaValidator")
class FacturaValidatorTest {

    private final FacturaValidator validator = new FacturaValidator();

    private OrdenTrabajo ordenConTotal(EstadoOrdenTrabajo estado, BigDecimal total) {
        ItemOrdenTrabajo item = ItemOrdenTrabajo.builder()
                .cantidad(1)
                .precioUnitario(total)
                .build();
        return OrdenTrabajo.builder()
                .id(1L)
                .estado(estado)
                .items(List.of(item))
                .build();
    }

    private Factura facturaActiva(TipoFactura tipoFactura, BigDecimal monto) {
        return Factura.builder()
                .estado(EstadoFactura.EMITIDA)
                .tipoFactura(tipoFactura)
                .montoFacturado(monto)
                .build();
    }

    @Test
    @DisplayName("Permite facturar una seña cuando la orden esta INGRESADO")
    void permiteSeniaEnIngresado() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), List.of()));
    }

    @Test
    @DisplayName("Permite facturar una seña cuando la orden esta EN_REPARACION")
    void permiteSeniaEnReparacion() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.EN_REPARACION, BigDecimal.valueOf(1000));

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), List.of()));
    }

    @Test
    @DisplayName("Rechaza la seña cuando la orden ya esta FINALIZADO/ENTREGADO/CANCELADO")
    void rechazaSeniaFueraDeIngresadoOEnReparacion() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.FINALIZADO, BigDecimal.valueOf(1000));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), List.of()));
    }

    @Test
    @DisplayName("Rechaza una segunda seña para la misma orden")
    void rechazaSeniaDuplicada() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));
        List<Factura> facturasActivas = List.of(facturaActiva(TipoFactura.SENIA, BigDecimal.valueOf(200)));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), facturasActivas));
    }

    @Test
    @DisplayName("Rechaza la factura final cuando la orden no esta FINALIZADO/ENTREGADO")
    void rechazaFinalFueraDeEstadoValido() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(1000), List.of()));
    }

    @Test
    @DisplayName("Permite la factura final por el saldo restante luego de una seña")
    void permiteFinalPorSaldoRestante() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.FINALIZADO, BigDecimal.valueOf(1000));
        List<Factura> facturasActivas = List.of(facturaActiva(TipoFactura.SENIA, BigDecimal.valueOf(300)));

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(700), facturasActivas));
    }

    @Test
    @DisplayName("Rechaza cuando el monto supera el saldo pendiente de la orden")
    void rechazaMontoQueSuperaSaldo() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.FINALIZADO, BigDecimal.valueOf(1000));
        List<Factura> facturasActivas = List.of(facturaActiva(TipoFactura.SENIA, BigDecimal.valueOf(300)));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(800), facturasActivas));
    }

    @Test
    @DisplayName("Rechaza una segunda factura final para la misma orden")
    void rechazaFinalDuplicada() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.FINALIZADO, BigDecimal.valueOf(1000));
        List<Factura> facturasActivas = List.of(facturaActiva(TipoFactura.FINAL, BigDecimal.valueOf(1000)));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(500), facturasActivas));
    }

    @Test
    @DisplayName("Permite facturar un monto exactamente igual al saldo pendiente (el limite del validador es estricto: solo rechaza cuando el monto SUPERA el saldo)")
    void permiteMontoIgualAlSaldoPendiente() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(1000), List.of()));
    }

    @Test
    @DisplayName("Rechaza una factura final que no cubre el saldo pendiente completo")
    void rechazaFinalQueNoCubreElSaldoTotal() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.FINALIZADO, BigDecimal.valueOf(1000));
        List<Factura> facturasActivas = List.of(facturaActiva(TipoFactura.SENIA, BigDecimal.valueOf(300)));

        assertThrows(BusinessRunTimeException.class, () ->
                validator.validarNuevaFactura(orden, TipoFactura.FINAL, BigDecimal.valueOf(500), facturasActivas));
    }

    @Test
    @DisplayName("Permite facturar una seña por menos del saldo pendiente (no exige cubrir el 100%)")
    void permiteSeniaParcialSinCubrirElTotal() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), List.of()));
    }

    @Test
    @DisplayName("Permite una nueva seña del mismo tipo cuando la anterior fue anulada")
    void permiteNuevaSeniaCuandoLaAnteriorFueAnulada() {
        OrdenTrabajo orden = ordenConTotal(EstadoOrdenTrabajo.INGRESADO, BigDecimal.valueOf(1000));
        Factura seniaAnulada = Factura.builder()
                .estado(EstadoFactura.ANULADA)
                .tipoFactura(TipoFactura.SENIA)
                .montoFacturado(BigDecimal.valueOf(200))
                .build();

        List<Factura> facturasActivas = List.of(seniaAnulada).stream()
                .filter(factura -> factura.getEstado() == EstadoFactura.EMITIDA)
                .toList();

        assertDoesNotThrow(() ->
                validator.validarNuevaFactura(orden, TipoFactura.SENIA, BigDecimal.valueOf(300), facturasActivas));
    }
}
