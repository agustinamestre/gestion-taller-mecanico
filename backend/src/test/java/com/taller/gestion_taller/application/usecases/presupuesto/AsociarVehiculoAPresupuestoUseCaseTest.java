package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.application.command.cliente.RegistrarClienteCommand;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand.DatosClienteNuevo;
import com.taller.gestion_taller.application.command.presupuesto.AsociarVehiculoAPresupuestoCommand.DatosVehiculoNuevo;
import com.taller.gestion_taller.application.command.vehiculo.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.usecases.cliente.RegistrarCliente;
import com.taller.gestion_taller.application.usecases.vehiculo.RegistrarVehiculo;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.model.SituacionIva;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsociarVehiculoAPresupuestoUseCaseTest {

    private static final Long PRESUPUESTO_ID = 1L;

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private RegistrarVehiculo registrarVehiculoUseCase;

    @Mock
    private RegistrarCliente registrarClienteUseCase;

    @InjectMocks
    private AsociarVehiculoAPresupuestoUseCase useCase;

    private static Presupuesto presupuestoPendienteSinVehiculo() {
        return Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .estado(EstadoPresupuesto.PENDIENTE)
                .vehiculo(null)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Debe asociar un vehiculo existente al presupuesto")
    void debeAsociarVehiculoExistenteExitosamente() {
        Presupuesto presupuesto = presupuestoPendienteSinVehiculo();
        Cliente cliente = Cliente.builder().id(3L).dni("12345678").build();
        Vehiculo vehiculo = Vehiculo.builder().id(5L).patente("ABC123").cliente(cliente).build();

        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, 5L, null, null, null);

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(vehiculoRepository.findById(5L)).thenReturn(Optional.of(vehiculo));
        when(presupuestoRepository.save(presupuesto)).thenReturn(presupuesto);

        Presupuesto resultado = useCase.asociar(command);

        assertEquals(vehiculo, resultado.getVehiculo());
        verify(presupuestoRepository).save(presupuesto);
        verify(registrarVehiculoUseCase, never()).registrar(any());
        verify(registrarClienteUseCase, never()).registrarCliente(any());
    }

    @Test
    @DisplayName("Debe crear un vehiculo nuevo para un cliente existente y asociarlo")
    void debeCrearVehiculoNuevoConClienteExistente() {
        Presupuesto presupuesto = presupuestoPendienteSinVehiculo();
        DatosVehiculoNuevo datosVehiculo = new DatosVehiculoNuevo("XYZ789", 3L, 2022, 0);

        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, null, datosVehiculo, 7L, null);

        Cliente clienteExistente = Cliente.builder().id(7L).dni("12345678").build();
        Vehiculo vehiculoNuevo = Vehiculo.builder().id(99L).patente("XYZ789").cliente(clienteExistente).build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(registrarVehiculoUseCase.registrar(new RegistrarVehiculoCommand("XYZ789", 3L, 2022, 7L, 0, null)))
                .thenReturn(vehiculoNuevo);
        when(presupuestoRepository.save(presupuesto)).thenReturn(presupuesto);

        Presupuesto resultado = useCase.asociar(command);

        assertEquals(vehiculoNuevo, resultado.getVehiculo());
        verify(registrarClienteUseCase, never()).registrarCliente(any());
        verify(vehiculoRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Debe crear un cliente nuevo y un vehiculo nuevo y asociarlo")
    void debeCrearClienteYVehiculoNuevosYAsociarlos() {
        Presupuesto presupuesto = presupuestoPendienteSinVehiculo();
        DatosClienteNuevo datosCliente = new DatosClienteNuevo(
                "12345678", "Juan", "Perez", "1122334455", "juan@gmail.com", "Calle 1", SituacionIva.CONSUMIDOR_FINAL);
        DatosVehiculoNuevo datosVehiculo = new DatosVehiculoNuevo("XYZ789", 3L, 2022, 0);

        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, null, datosVehiculo, null, datosCliente);

        Cliente clienteNuevo = Cliente.builder().id(50L).dni("12345678").build();
        Vehiculo vehiculoNuevo = Vehiculo.builder().id(99L).patente("XYZ789").cliente(clienteNuevo).build();

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(registrarClienteUseCase.registrarCliente(new RegistrarClienteCommand(
                "12345678", "Juan", "Perez", "1122334455", "juan@gmail.com", "Calle 1", SituacionIva.CONSUMIDOR_FINAL)))
                .thenReturn(clienteNuevo);
        when(registrarVehiculoUseCase.registrar(new RegistrarVehiculoCommand("XYZ789", 3L, 2022, 50L, 0, null)))
                .thenReturn(vehiculoNuevo);
        when(presupuestoRepository.save(presupuesto)).thenReturn(presupuesto);

        Presupuesto resultado = useCase.asociar(command);

        assertEquals(vehiculoNuevo, resultado.getVehiculo());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando el presupuesto no existe")
    void debeLanzarNotFoundExceptionCuandoElPresupuestoNoExiste() {
        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, 5L, null, null, null);

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> useCase.asociar(command));

        assertEquals("PRESUPUESTO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando el vehiculo indicado no existe")
    void debeLanzarNotFoundExceptionCuandoElVehiculoNoExiste() {
        Presupuesto presupuesto = presupuestoPendienteSinVehiculo();

        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, 5L, null, null, null);

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(vehiculoRepository.findById(5L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> useCase.asociar(command));

        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el presupuesto ya tiene un vehiculo asociado")
    void debeLanzarExcepcionCuandoElPresupuestoYaTieneVehiculo() {
        Vehiculo vehiculoYaAsociado = Vehiculo.builder().id(1L).patente("AAA111").build();
        Presupuesto presupuesto = Presupuesto.builder()
                .id(PRESUPUESTO_ID)
                .estado(EstadoPresupuesto.PENDIENTE)
                .vehiculo(vehiculoYaAsociado)
                .items(new ArrayList<>())
                .build();

        Vehiculo vehiculo = Vehiculo.builder().id(5L).patente("ABC123").build();

        AsociarVehiculoAPresupuestoCommand command =
                new AsociarVehiculoAPresupuestoCommand(PRESUPUESTO_ID, 5L, null, null, null);

        when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
        when(vehiculoRepository.findById(5L)).thenReturn(Optional.of(vehiculo));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () -> useCase.asociar(command));

        assertEquals("PRESUPUESTO_YA_TIENE_VEHICULO", exception.getBusinessError().code());
        verify(presupuestoRepository, never()).save(any());
    }
}
