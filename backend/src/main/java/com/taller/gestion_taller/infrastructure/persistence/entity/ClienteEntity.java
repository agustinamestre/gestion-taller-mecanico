package com.taller.gestion_taller.infrastructure.persistence.entity;

import com.taller.gestion_taller.domain.model.SituacionIva;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String telefono;

    @Column(length = 150)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacion_iva", nullable = false)
    private SituacionIva situacionIva;

    @Column(length = 255)
    private String direccion;

    @Column(nullable = false)
    private boolean activo;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDate fechaModificacion;
}
