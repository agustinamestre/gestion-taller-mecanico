package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class EliminarPresupuestosVencidosYRechazadosUseCase implements EliminarPresupuestosVencidosYRechazados {

    private static final int DIAS_RETENCION_POST_VENCIMIENTO = 180;
    private static final List<EstadoPresupuesto> ESTADOS_ELIMINABLES =
            List.of(EstadoPresupuesto.VENCIDO, EstadoPresupuesto.RECHAZADO);

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public int eliminar() {
        LocalDate limite = LocalDate.now().minusDays(DIAS_RETENCION_POST_VENCIMIENTO);

        List<Presupuesto> aEliminar = presupuestoRepository
                .findByEstadoInAndFechaVencimientoBefore(ESTADOS_ELIMINABLES, limite);

        aEliminar.forEach(p -> presupuestoRepository.deleteById(p.getId()));

        return aEliminar.size();
    }
}