package com.taller.gestion_taller.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String patente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private ModeloEntity modelo;

    @Column(nullable = false)
    private Integer anio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "fecha_ultimo_service")
    private LocalDate fechaUltimoService;

    @Column(name = "km_ultimo_service")
    private Integer kmUltimoService;

    @Column(name = "kilometraje_actual", nullable = false)
    private Integer kilometrajeActual;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "kilometraje_alta")
    private Integer kilometrajeAlta;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;
}
