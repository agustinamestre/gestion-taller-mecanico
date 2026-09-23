package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class MarcarPresupuestosVencidosUseCase implements MarcarPresupuestosVencidos {

    private static final List<EstadoPresupuesto> ESTADOS_VENCIBLES =
            List.of(EstadoPresupuesto.PENDIENTE, EstadoPresupuesto.APROBADO);

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public int marcar() {
        List<Presupuesto> vencidos = presupuestoRepository.findByEstadoInAndFechaVencimientoBefore(
                ESTADOS_VENCIBLES, LocalDate.now());

        vencidos.forEach(Presupuesto::marcarComoVencido);
        vencidos.forEach(presupuestoRepository::save);

        return vencidos.size();
    }
}