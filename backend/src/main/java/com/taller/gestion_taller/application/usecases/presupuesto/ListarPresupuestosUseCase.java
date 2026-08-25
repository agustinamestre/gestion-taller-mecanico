package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarPresupuestosUseCase implements ListarPresupuestos {

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public List<Presupuesto> listar(String patente) {
        if (patente == null || patente.isBlank()) {
            return presupuestoRepository.findAll();
        }
        return presupuestoRepository.findByPatente(patente);
    }
}
