package com.taller.gestion_taller.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modelos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "marca_id"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModeloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaEntity marca;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

}
