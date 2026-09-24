package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Factura {

    private Long id;
    private OrdenTrabajo ordenTrabajo;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private FormaPago formaPago;
    private EstadoFactura estado;
    private String motivoAnulacion;
    private TipoComprobante tipoComprobante;

    public static Factura crearNueva(OrdenTrabajo ordenTrabajo, FormaPago formaPago, String numeroFactura,
                                      TipoComprobante tipoComprobante) {
        return Factura.builder()
                .ordenTrabajo(ordenTrabajo)
                .formaPago(formaPago)
                .numeroFactura(numeroFactura)
                .tipoComprobante(tipoComprobante)
                .fechaEmision(LocalDate.now())
                .estado(EstadoFactura.EMITIDA)
                .build();
    }

    public void anular(String motivo) {
        if (!this.estado.puedeTransicionarA(EstadoFactura.ANULADA)) {
            throw new BusinessRunTimeException(BusinessErrors.facturaYaAnulada(this.id));
        }
        if (StringUtils.isBlank(motivo)) {
            throw new BusinessRunTimeException(BusinessErrors.facturaSinMotivoAnulacion());
        }
        this.estado = EstadoFactura.ANULADA;
        this.motivoAnulacion = motivo;
    }

    public String getClienteDni() {
        return ordenTrabajo.getVehiculo().getCliente().getDni();
    }

    public BigDecimal getTotal() {
        return ordenTrabajo.calcularTotal();
    }
}
