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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarClienteUseCase - Búsqueda por DNI")
class ListarClienteUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ListarClienteUseCase useCase;

    @Test
    @DisplayName("Debe retornar cliente cuando existe con el DNI buscado")
    void debeRetornarClienteCuandoExiste() {
        String dni = "12345678";
        Cliente clienteEsperado = Cliente.builder()
                .id(1L)
                .dni(dni)
                .nombre("Juan")
                .apellido("Perez")
                .telefono("3364249176")
                .email("juan@gmail.com")
                .direccion("Calle Falsa 123")
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .fechaModificacion(LocalDate.now())
                .build();

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(clienteEsperado));

        // When
        Cliente resultado = useCase.listarCliente(dni);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getDni()).isEqualTo(dni);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Perez");
        assertThat(resultado.getTelefono()).isEqualTo("3364249176");
        assertThat(resultado.getEmail()).isEqualTo("juan@gmail.com");
        assertThat(resultado.getDireccion()).isEqualTo("Calle Falsa 123");
        assertThat(resultado.isActivo()).isTrue();
        
        verify(clienteRepository, times(1)).findByDni(dni);
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando no existe cliente con el DNI")
    void debeLanzarExcepcionCuandoNoExiste() {
        String dni = "99999999";
        when(clienteRepository.findByDni(dni)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.listarCliente(dni);
        });

        assertTrue(exception.getMessage().contains("No se encontro un cliente con DNI: 99999999"));;
        verify(clienteRepository, times(1)).findByDni(dni);
    }
}
