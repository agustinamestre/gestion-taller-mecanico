package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class MarcarPresupuestosVencidosUseCase implements MarcarPresupuestosVencidos {

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public int marcar() {
        List<Presupuesto> pendientesVencidos = presupuestoRepository.findByEstadoAndFechaVencimientoBefore(
                EstadoPresupuesto.PENDIENTE, LocalDate.now());

        pendientesVencidos.forEach(Presupuesto::marcarComoVencido);
        pendientesVencidos.forEach(presupuestoRepository::save);

        return pendientesVencidos.size();
    }
}
