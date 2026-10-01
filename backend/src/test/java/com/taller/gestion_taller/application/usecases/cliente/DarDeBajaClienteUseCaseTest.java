package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DarDeBajaClienteUseCase")
class DarDeBajaClienteUseCaseTest {

    private static final String DNI = "12345678";
    private static final Long CLIENTE_ID = 1L;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private DarDeBajaClienteUseCase useCase;

    @Test
    @DisplayName("Dar de baja un cliente exitosamente")
    void debeDarDeBajaClienteExitosamente() {
        Cliente cliente = mock(Cliente.class);

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.of(cliente));
        when(cliente.darDeBaja()).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(cliente.getId()).thenReturn(CLIENTE_ID);
        when(vehiculoRepository.findByClienteId(CLIENTE_ID)).thenReturn(List.of());

        useCase.darDeBaja(DNI);

        verify(clienteRepository).findByDni(DNI);
        verify(cliente).darDeBaja();
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Dar de baja un cliente debe desactivar en cascada sus vehiculos activos")
    void debeDesactivarVehiculosActivosEnCascada() {
        Cliente cliente = mock(Cliente.class);
        Vehiculo vehiculoActivo = mock(Vehiculo.class);
        Vehiculo vehiculoInactivo = mock(Vehiculo.class);
        Vehiculo vehiculoDesactivado = mock(Vehiculo.class);

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.of(cliente));
        when(cliente.darDeBaja()).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(cliente.getId()).thenReturn(CLIENTE_ID);
        when(vehiculoActivo.isActivo()).thenReturn(true);
        when(vehiculoInactivo.isActivo()).thenReturn(false);
        when(vehiculoActivo.desactivar()).thenReturn(vehiculoDesactivado);
        when(vehiculoRepository.findByClienteId(CLIENTE_ID)).thenReturn(List.of(vehiculoActivo, vehiculoInactivo));

        useCase.darDeBaja(DNI);

        verify(vehiculoActivo).desactivar();
        verify(vehiculoInactivo, never()).desactivar();
        verify(vehiculoRepository).save(vehiculoDesactivado);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el cliente a dar de baja no existe")
    void debeLanzarExcepcionCuandoClienteNoExiste() {

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.darDeBaja(DNI);
        });

        assertTrue(exception.getMessage().contains("No se encontro un cliente con DNI: 12345678"));;
        verify(clienteRepository, never()).save(any());
    }
}
