package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.MedioContacto;
import com.taller.gestion_taller.domain.model.TipoAlerta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDate fechaAlerta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAlerta tipo;

    @Column(nullable = false)
    private boolean contactado;

    @Column(name = "fecha_contacto")
    private LocalDate fechaContacto;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_contacto", length = 20)
    private MedioContacto medioContacto;

    @Column(length = 500)
    private String observaciones;
}
