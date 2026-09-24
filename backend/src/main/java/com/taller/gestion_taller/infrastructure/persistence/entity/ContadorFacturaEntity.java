package com.taller.gestion_taller.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contador_factura")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContadorFacturaEntity {

    @Id
    private Long id;

    private Long ultimoNumero;
}
