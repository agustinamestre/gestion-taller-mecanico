package com.taller.gestion_taller.domain.service;

import com.taller.gestion_taller.domain.model.MotivoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;

import java.time.LocalDate;
import java.util.Optional;

public class PoliticaServiceVehiculo {

    private final int mesesEntreServices;
    private final int kmEntreServices;

    public PoliticaServiceVehiculo(int mesesEntreServices, int kmEntreServices) {
        this.mesesEntreServices = mesesEntreServices;
        this.kmEntreServices = kmEntreServices;
    }

    public Optional<MotivoAlerta> evaluar(Vehiculo vehiculo, LocalDate hoy) {
        LocalDate fechaReferencia = vehiculo.fechaReferenciaService();
        if (fechaReferencia != null && !fechaReferencia.plusMonths(mesesEntreServices).isAfter(hoy)) {
            return Optional.of(MotivoAlerta.TIEMPO);
        }

        Integer kmReferencia = vehiculo.kmReferenciaService();
        Integer kmActual = vehiculo.getKilometrajeActual();
        if (kmReferencia != null && kmActual != null && kmActual - kmReferencia >= kmEntreServices) {
            return Optional.of(MotivoAlerta.KILOMETRAJE);
        }

        return Optional.empty();
    }
}
