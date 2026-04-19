package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.TipoProducto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "tipo"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProducto tipo;

    @Column(name = "precio_actual")
    private BigDecimal precioActual;

    @Column(name = "stock_actual")
    private Integer stockActual;

    @Builder.Default
    private boolean activo = true;
}
