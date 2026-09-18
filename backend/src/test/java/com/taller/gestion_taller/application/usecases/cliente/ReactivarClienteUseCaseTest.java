package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReactivarClienteUseCase")
class ReactivarClienteUseCaseTest {

    private static final String DNI = "12345678";

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ReactivarClienteUseCase useCase;

    @Test
    @DisplayName("Reactivar un cliente exitosamente")
    void debeReactivarClienteExitosamente() {
        Cliente cliente = mock(Cliente.class);

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.of(cliente));
        when(cliente.reactivar()).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        useCase.reactivar(DNI);

        verify(clienteRepository).findByDni(DNI);
        verify(cliente).reactivar();
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Lanzar excepcion cuando el cliente a reactivar no existe")
    void debeLanzarExcepcionCuandoClienteNoExiste() {

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.reactivar(DNI);
        });

        assertTrue(exception.getMessage().contains("No se encontro un cliente con DNI: 12345678"));
        verify(clienteRepository, never()).save(any());
    }
}
