package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.RegistrarVehiculoCommand;
import com.taller.gestion_taller.application.mapper.VehiculoApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessError;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.VehiculoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrarVehiculoUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private ModeloRepository modeloRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private VehiculoValidator vehiculoValidator;
    @Mock
    private VehiculoApplicationMapper vehiculoApplicationMapper;

    @InjectMocks
    private RegistrarVehiculoUseCase registrarVehiculoUseCase;

    private RegistrarVehiculoCommand command;
    private Cliente cliente;
    private Modelo modelo;
    private Vehiculo vehiculoDePrueba;

    @BeforeEach
    void setUp() {
        command = new RegistrarVehiculoCommand("ABC-123", 1L, 2020, 1L, 15000);
        cliente = Cliente.builder().id(1L).activo(true).build();
        Marca marca = Marca.builder().id(1L).activo(true).build();
        modelo = Modelo.builder().id(1L).marca(marca).activo(true).build();

        vehiculoDePrueba = Vehiculo.builder()
                .patente(command.patente())
                .modelo(modelo)
                .anio(command.anio())
                .cliente(cliente)
                .kilometrajeActual(command.kilometrajeActual())
                .build();
    }

    @Test
    void registrar_DebeRegistrarVehiculo_CuandoLosDatosSonValidos() {
        // Arrange
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.of(modelo));
        when(vehiculoApplicationMapper.commandToDomain(command, modelo, cliente)).thenReturn(vehiculoDePrueba);
        Vehiculo vehiculoGuardado = vehiculoDePrueba.toBuilder().id(1L).build();
        when(vehiculoRepository.save(vehiculoDePrueba)).thenReturn(vehiculoGuardado);

        // Act
        Vehiculo result = registrarVehiculoUseCase.registrar(command);

        // Assert
        assertNotNull(result);
        assertEquals(vehiculoGuardado.getId(), result.getId());
        assertEquals(command.patente(), result.getPatente());
        assertEquals(command.anio(), result.getAnio());
        verify(vehiculoValidator).validarPatenteUnica(command.patente());
        verify(vehiculoRepository).save(vehiculoDePrueba);
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoLaPatenteEstaDuplicada() {
        // Arrange
        doThrow(new BusinessRunTimeException(new BusinessError("VEHICULO_PATENTE_DUPLICADA", "error")))
                .when(vehiculoValidator).validarPatenteUnica(command.patente());

        // Act & Assert
        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () -> registrarVehiculoUseCase.registrar(command));
        assertEquals("VEHICULO_PATENTE_DUPLICADA", exception.getBusinessError().code());
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoElClienteNoExiste() {
        // Arrange
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.empty());
    
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> registrarVehiculoUseCase.registrar(command));
        assertEquals("CLIENTE_NO_ENCONTRADO", exception.getBusinessError().code());
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoElClienteEstaInactivo() {
        // Arrange
        cliente = cliente.toBuilder().activo(false).build();
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));

        // Act & Assert
        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () -> registrarVehiculoUseCase.registrar(command));
        assertEquals("CLIENTE_INACTIVO", exception.getBusinessError().code());
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoElModeloNoExiste() {
        // Arrange
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.empty());
    
        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> registrarVehiculoUseCase.registrar(command));
        assertEquals("MODELO_NO_ENCONTRADO", exception.getBusinessError().code());
    }

    @Test
    void registrar_DebeLanzarExcepcion_CuandoElModeloEstaInactivo() {
        // Arrange
        modelo = modelo.toBuilder().activo(false).build();
        when(clienteRepository.findById(command.clienteId())).thenReturn(Optional.of(cliente));
        when(modeloRepository.findById(command.modeloId())).thenReturn(Optional.of(modelo));

        // Act & Assert
        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class, () -> registrarVehiculoUseCase.registrar(command));
        assertEquals("MODELO_YA_DESACTIVADO", exception.getBusinessError().code());
    }
}
