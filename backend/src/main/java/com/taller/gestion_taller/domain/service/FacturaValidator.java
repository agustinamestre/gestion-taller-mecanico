package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.*;

import java.math.BigDecimal;
import java.util.List;

public class FacturaValidator {

    public void validarNuevaFactura(OrdenTrabajo orden, TipoFactura tipoFactura, BigDecimal monto,
                                     List<Factura> facturasActivasDeLaOrden) {
        validarEstadoSegunTipo(orden, tipoFactura);
        validarMontoSeniaPositivo(tipoFactura, monto);
        validarNoDuplicarTipo(orden, tipoFactura, facturasActivasDeLaOrden);
        validarFinalConSaldoPendiente(tipoFactura, orden, facturasActivasDeLaOrden);
        validarNoSuperarTotal(orden, monto, facturasActivasDeLaOrden);
        validarFinalCubreSaldoTotal(tipoFactura, orden, monto, facturasActivasDeLaOrden);
    }

    private void validarEstadoSegunTipo(OrdenTrabajo orden, TipoFactura tipoFactura) {
        if (tipoFactura == TipoFactura.SENIA) {
            if (orden.getEstado() != EstadoOrdenTrabajo.INGRESADO && orden.getEstado() != EstadoOrdenTrabajo.EN_REPARACION) {
                throw new BusinessRunTimeException(BusinessErrors.ordenNoFacturableComoSenia(orden.getEstado()));
            }
        } else {
            if (orden.getEstado() != EstadoOrdenTrabajo.FINALIZADO && orden.getEstado() != EstadoOrdenTrabajo.ENTREGADO) {
                throw new BusinessRunTimeException(BusinessErrors.ordenNoFacturable(orden.getEstado()));
            }
        }
    }

    private void validarMontoSeniaPositivo(TipoFactura tipoFactura, BigDecimal monto) {
        if (tipoFactura == TipoFactura.SENIA && monto.signum() <= 0) {
            throw new BusinessRunTimeException(BusinessErrors.montoSeniaDebeSerPositivo());
        }
    }

    private void validarNoDuplicarTipo(OrdenTrabajo orden, TipoFactura tipoFactura, List<Factura> facturasActivasDeLaOrden) {
        if (tipoFactura != TipoFactura.FINAL) {
            return;
        }
        boolean yaExiste = facturasActivasDeLaOrden.stream()
                .anyMatch(factura -> factura.getTipoFactura() == tipoFactura);
        if (yaExiste) {
            throw new BusinessRunTimeException(BusinessErrors.ordenYaTieneFacturaDeTipo(orden.getId(), tipoFactura));
        }
    }

    private void validarFinalConSaldoPendiente(TipoFactura tipoFactura, OrdenTrabajo orden,
                                               List<Factura> facturasActivasDeLaOrden) {
        if (tipoFactura != TipoFactura.FINAL) {
            return;
        }
        BigDecimal saldoPendiente = Factura.calcularSaldoPendiente(orden.calcularTotal(), facturasActivasDeLaOrden);
        if (saldoPendiente.signum() <= 0) {
            throw new BusinessRunTimeException(BusinessErrors.ordenSinSaldoPendiente());
        }
    }

    private void validarNoSuperarTotal(OrdenTrabajo orden, BigDecimal monto, List<Factura> facturasActivasDeLaOrden) {
        BigDecimal saldoPendiente = Factura.calcularSaldoPendiente(orden.calcularTotal(), facturasActivasDeLaOrden);
        if (monto.compareTo(saldoPendiente) > 0) {
            throw new BusinessRunTimeException(BusinessErrors.montoFacturaSuperaSaldoPendiente(saldoPendiente));
        }
    }

    private void validarFinalCubreSaldoTotal(TipoFactura tipoFactura, OrdenTrabajo orden, BigDecimal monto,
                                              List<Factura> facturasActivasDeLaOrden) {
        if (tipoFactura != TipoFactura.FINAL) {
            return;
        }
        BigDecimal saldoPendiente = Factura.calcularSaldoPendiente(orden.calcularTotal(), facturasActivasDeLaOrden);
        if (monto.compareTo(saldoPendiente) < 0) {
            throw new BusinessRunTimeException(BusinessErrors.facturaFinalDebeCubrirSaldoTotal(saldoPendiente));
        }
    }
}
