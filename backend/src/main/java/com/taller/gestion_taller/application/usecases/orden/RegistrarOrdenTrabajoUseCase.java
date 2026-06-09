package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarOrdenTrabajoUseCase implements RegistrarOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PresupuestoRepository presupuestoRepository;

    @Override
    public OrdenTrabajo registrar(RegistrarOrdenTrabajoCommand command) {
        OrdenTrabajo orden = command.presupuestoId() != null
                ? construirDesdePresupuesto(command)
                : construirDesdePatente(command);

        return ordenTrabajoRepository.save(orden);
    }

    private OrdenTrabajo construirDesdePresupuesto(RegistrarOrdenTrabajoCommand command) {
        Presupuesto presupuesto = presupuestoRepository.findById(command.presupuestoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(command.presupuestoId())));

        validarPresupuestoConvertibleAOrden(presupuesto);
        validarCoherenciaPatente(command.patente(), presupuesto.getVehiculo());

        return OrdenTrabajo.crearNueva(
                presupuesto.getVehiculo(),
                presupuesto,
                command.descripcionProblema(),
                command.usuarioCreacionId());
    }

    private OrdenTrabajo construirDesdePatente(RegistrarOrdenTrabajoCommand command) {
        Vehiculo vehiculo = vehiculoRepository.findByPatente(command.patente())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.vehiculoNoEncontrado(command.patente())));

        return OrdenTrabajo.crearNueva(
                vehiculo,
                null,
                command.descripcionProblema(),
                command.usuarioCreacionId());
    }

    private void validarPresupuestoConvertibleAOrden(Presupuesto presupuesto) {
        if (presupuesto.getVehiculo() == null) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoSinVehiculoAsociado());
        }
        if (presupuesto.getEstado() != EstadoPresupuesto.APROBADO) {
            throw new BusinessRunTimeException(BusinessErrors.presupuestoDebeEstarAprobado());
        }
    }

    /**
     * Si el comando incluye una patente ademas del presupuesto, debe coincidir con la del
     * vehiculo del presupuesto. Esto evita inconsistencias silenciosas en los datos cuando el
     * frontend o un cliente API envian informacion contradictoria.
     * <p>
     * La comparacion es case-insensitive y se ignora cuando la patente no fue provista.
     */
    private void validarCoherenciaPatente(String patenteRecibida, Vehiculo vehiculoDelPresupuesto) {
        if (patenteRecibida == null || patenteRecibida.isBlank()) {
            return;
        }
        String patentePresupuesto = vehiculoDelPresupuesto.getPatente();
        if (!patenteRecibida.equalsIgnoreCase(patentePresupuesto)) {
            throw new BusinessRunTimeException(
                    BusinessErrors.patenteInconsistenteConPresupuesto(patenteRecibida, patentePresupuesto));
        }
    }
}
