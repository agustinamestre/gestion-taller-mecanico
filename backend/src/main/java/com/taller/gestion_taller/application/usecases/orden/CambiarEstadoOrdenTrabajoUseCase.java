package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.application.command.orden.CambiarEstadoOrdenTrabajoCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CambiarEstadoOrdenTrabajoUseCase implements CambiarEstadoOrdenTrabajo {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final FacturaRepository facturaRepository;

    @Override
    public void cambiar(CambiarEstadoOrdenTrabajoCommand command) {
        OrdenTrabajo orden = ordenTrabajoRepository.findById(command.ordenId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.ordenNoEncontrada(command.ordenId())));

        orden.cambiarEstado(command.nuevoEstado());
        if (command.nuevoEstado() == EstadoOrdenTrabajo.ENTREGADO) {
            validarTotalmenteFacturada(orden);
        }
        ordenTrabajoRepository.save(orden);

        if (command.nuevoEstado() == EstadoOrdenTrabajo.CANCELADO && orden.getPresupuesto() != null) {
            cancelarPresupuesto(orden.getPresupuesto().getId());
        }

        if (orden.registraServiceAlEntregar()) {
            registrarServiceDelVehiculo(orden.getVehiculo().getId());
        }
    }

    private void validarTotalmenteFacturada(OrdenTrabajo orden) {
        BigDecimal saldoPendiente = Factura.calcularSaldoPendiente(orden.calcularTotal(),
                facturaRepository.findActivasByOrdenTrabajoId(orden.getId()));
        if (saldoPendiente.signum() > 0) {
            throw new BusinessRunTimeException(BusinessErrors.ordenConSaldoPendiente(saldoPendiente));
        }
    }

    // Reinicia el ciclo de alertas: la fecha y el km del service pasan a ser los de la entrega.
    private void registrarServiceDelVehiculo(Long vehiculoId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new NotFoundException(BusinessErrors.vehiculoNoEncontrado(vehiculoId)));

        vehiculoRepository.save(vehiculo.registrarService(LocalDate.now()));
    }

    private void cancelarPresupuesto(Long presupuestoId) {
        Presupuesto presupuesto = presupuestoRepository.findById(presupuestoId)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(presupuestoId)));

        presupuesto.cancelar();
        presupuestoRepository.save(presupuesto);
    }
}