package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_trabajo")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTrabajoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private VehiculoEntity vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presupuesto_id")
    private PresupuestoEntity presupuesto;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_egreso")
    private LocalDate fechaEgreso;

    @Column(name = "descripcion_problema", nullable = false, length = 500)
    private String descripcionProblema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrdenTrabajo estado;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemOrdenTrabajoEntity> items = new ArrayList<>();
}
