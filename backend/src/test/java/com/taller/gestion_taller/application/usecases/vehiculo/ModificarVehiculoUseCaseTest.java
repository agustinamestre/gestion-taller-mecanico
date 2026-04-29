package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ModificarVehiculoCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModificarVehiculoUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private ModeloRepository modeloRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ModificarVehiculoUseCase modificarVehiculoUseCase;

    private static final Long VEHICULO_ID = 1L;

    private ModificarVehiculoCommand command;
    private Cliente cliente;
    private Modelo modelo;
    private Vehiculo vehiculoExistente;

    @BeforeEach
    void setUp() {
        command = new ModificarVehiculoCommand(2L, 2022, 2L);

        cliente = Cliente.builder().id(2L).activo(true).build();
        Marca marca = Marca.builder().id(1L).activo(true).build();
        modelo = Modelo.builder().id(2L).marca(marca).activo(true).build();

        Cliente clienteOriginal = Cliente.builder().id(1L).activo(true).build();
        Modelo modeloOriginal = Modelo.builder().id(1L).marca(marca).activo(true).build();

        vehiculoExistente = Vehiculo.builder()
                .id(VEHICULO_ID)
                .patente("ABC-123")
                .modelo(modeloOriginal)
                .anio(2020)
                .cliente(clienteOriginal)
                .kilometrajeActual(15000)
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("debe modificar vehiculo cuando los datos son validos")
    void debeModificarVehiculoCuandoLosDatosSonValidos() {
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.of(modelo));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehiculo resultado = modificarVehiculoUseCase.modificar(VEHICULO_ID, command);

        assertNotNull(resultado);
        assertEquals("ABC-123", resultado.getPatente());
        assertEquals(command.anio(), resultado.getAnio());
        assertEquals(15000, resultado.getKilometrajeActual(), "El kilometraje no debe modificarse en este endpoint");
        assertEquals(cliente.getId(), resultado.getCliente().getId());
        assertEquals(modelo.getId(), resultado.getModelo().getId());
        verify(vehiculoRepository).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el vehiculo no existe")
    void debeLanzarNotFoundExceptionCuandoElVehiculoNoExiste() {
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> modificarVehiculoUseCase.modificar(VEHICULO_ID, command));
        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el cliente no existe")
    void debeLanzarNotFoundExceptionCuandoElClienteNoExiste() {
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> modificarVehiculoUseCase.modificar(VEHICULO_ID, command));
        assertEquals("CLIENTE_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar excepcion cuando el cliente esta inactivo")
    void debeLanzarExcepcionCuandoElClienteEstaInactivo() {
        cliente = cliente.toBuilder().activo(false).build();
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class,
                () -> modificarVehiculoUseCase.modificar(VEHICULO_ID, command));
        assertEquals("CLIENTE_INACTIVO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el modelo no existe")
    void debeLanzarNotFoundExceptionCuandoElModeloNoExiste() {
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> modificarVehiculoUseCase.modificar(VEHICULO_ID, command));
        assertEquals("MODELO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar excepcion cuando el modelo esta inactivo")
    void debeLanzarExcepcionCuandoElModeloEstaInactivo() {
        modelo = modelo.toBuilder().activo(false).build();
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.of(modelo));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class,
                () -> modificarVehiculoUseCase.modificar(VEHICULO_ID, command));
        assertEquals("MODELO_YA_DESACTIVADO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }
}
