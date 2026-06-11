package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.cliente.RegistrarClienteCommand;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand.DatosClienteNuevo;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand.DatosVehiculoNuevo;
import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculo;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsociarVehiculoAPresupuestoUseCase implements AsociarVehiculoAPresupuesto {

    private final PresupuestoRepository presupuestoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RegistrarVehiculo registrarVehiculoUseCase;
    private final RegistrarCliente registrarClienteUseCase;

    @Override
    public Presupuesto asociar(AsociarVehiculoAPresupuestoCommand command) {
        Presupuesto presupuesto = buscarPresupuesto(command.presupuestoId());
        Vehiculo vehiculo = resolverVehiculo(command);

        presupuesto.asociarVehiculo(vehiculo);

        Presupuesto guardado = presupuestoRepository.save(presupuesto);
        log.info("Vehículo {} asociado al presupuesto {}", vehiculo.getPatente(), guardado.getId());
        return guardado;
    }

    private Presupuesto buscarPresupuesto(Long presupuestoId) {
        return presupuestoRepository.findById(presupuestoId)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.presupuestoNoEncontrado(presupuestoId)));
    }

    private Vehiculo resolverVehiculo(AsociarVehiculoAPresupuestoCommand command) {
        if (command.tieneVehiculoExistente()) {
            return buscarVehiculoPorId(command.vehiculoId());
        }
        return crearVehiculoNuevo(command);
    }

    private Vehiculo buscarVehiculoPorId(Long vehiculoId) {
        return vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.vehiculoNoEncontrado(vehiculoId)));
    }

    private Vehiculo crearVehiculoNuevo(AsociarVehiculoAPresupuestoCommand command) {
        Long clienteId = resolverClienteId(command);
        DatosVehiculoNuevo datosVehiculo = command.datosVehiculoNuevo();

        return registrarVehiculoUseCase.registrar(new RegistrarVehiculoCommand(
                datosVehiculo.patente(),
                datosVehiculo.modeloId(),
                datosVehiculo.anio(),
                clienteId,
                datosVehiculo.kilometrajeActual()
        ));
    }

    private Long resolverClienteId(AsociarVehiculoAPresupuestoCommand command) {
        if (command.tieneClienteExistente()) {
            return command.clienteId();
        }
        return crearClienteNuevo(command.datosClienteNuevo()).getId();
    }

    private Cliente crearClienteNuevo(DatosClienteNuevo datos) {
        return registrarClienteUseCase.registrarCliente(new RegistrarClienteCommand(
                datos.dni(),
                datos.nombre(),
                datos.apellido(),
                datos.telefono(),
                datos.email(),
                datos.direccion()
        ));
    }
}
