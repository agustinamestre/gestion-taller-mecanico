package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ListarPresupuestosUseCase implements ListarPresupuestos {

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public List<Presupuesto> listar(String patente, LocalDate fechaDesde, LocalDate fechaHasta) {
        boolean tienePatente = patente != null && !patente.isBlank();
        boolean tieneFechas = fechaDesde != null && fechaHasta != null;

        if (tienePatente && tieneFechas) {
            return presupuestoRepository.findByPatenteAndFechaEmisionBetween(patente, fechaDesde, fechaHasta);
        }
        if (tieneFechas) {
            return presupuestoRepository.findByFechaEmisionBetween(fechaDesde, fechaHasta);
        }
        if (tienePatente) {
            return presupuestoRepository.findByPatente(patente);
        }
        return presupuestoRepository.findAll();
    }
}
