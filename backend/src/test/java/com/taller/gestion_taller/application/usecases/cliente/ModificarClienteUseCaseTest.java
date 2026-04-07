package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.ModificarClienteCommand;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModificarClienteUseCase")
class ModificarClienteUseCaseTest {

    private static final String DNI = "12345678";

    private static final ModificarClienteCommand COMMAND = new ModificarClienteCommand(
            DNI, "Juan", "Perez", "3364249176", "juan@gmail.com", "Calle 123"
    );

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ModificarClienteUseCase useCase;

    @Test
    @DisplayName("debe retornar el cliente con los datos actualizados")
    void debeRetornarClienteActualizado() {
        Cliente clienteExistente = mock(Cliente.class);
        Cliente clienteActualizado = mock(Cliente.class);

        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.of(clienteExistente));
        when(clienteExistente.actualizarDatos(COMMAND)).thenReturn(clienteExistente);
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteActualizado);

        Cliente resultado = useCase.modificarCliente(DNI, COMMAND);

        assertThat(resultado).isEqualTo(clienteActualizado);
        verify(clienteExistente).actualizarDatos(COMMAND);
        verify(clienteRepository).save(clienteExistente);
    }


    @Test
    @DisplayName("Cuando el cliente no existe")
    void debeLanzarNotFoundException() {
        when(clienteRepository.findByDni(DNI)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.modificarCliente(DNI, COMMAND);
        });

        assertTrue(exception.getMessage().contains("No se encontro un cliente con DNI: 12345678"));
        verify(clienteRepository, never()).save(any());

    }
}