package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.TipoProducto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "items_orden_trabajo")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrdenTrabajoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenTrabajoEntity orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoProducto tipo;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Boolean facturado;
}