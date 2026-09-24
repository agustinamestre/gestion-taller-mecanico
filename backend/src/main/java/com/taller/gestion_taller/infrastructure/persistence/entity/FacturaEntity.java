package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.TipoComprobante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orden_trabajo_id", referencedColumnName = "id", unique = true)
    private OrdenTrabajoEntity ordenTrabajo;

    @Column(name = "numero_factura", unique = true)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false)
    private FormaPago formaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoFactura estado;

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", length = 1)
    private TipoComprobante tipoComprobante;

}
