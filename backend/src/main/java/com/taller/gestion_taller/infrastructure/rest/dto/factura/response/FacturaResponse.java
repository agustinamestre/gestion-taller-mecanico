package com.taller.gestion_taller.infrastructure.rest.dto.factura.response;

import com.taller.gestion_taller.domain.model.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacturaResponse {
    private Long id;
    private Long ordenTrabajoId;
    private String numeroFactura;
    private LocalDate fechaEmision;
    private FormaPago formaPago;
    private String clienteDni;
    private BigDecimal total;
}
