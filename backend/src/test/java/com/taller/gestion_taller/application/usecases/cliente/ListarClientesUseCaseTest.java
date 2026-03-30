package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarClientesUseCase")
class ListarClientesUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ListarClientesUseCase useCase;

    @Test
    @DisplayName("Debe retornar lista de clientes registrados")
    void debeRetornarListaDeClientes() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Juan")
                .apellido("Perez")
                .telefono("3364249176")
                .email("juan@gmail.com")
                .direccion("Calle Falsa 123")
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .fechaModificacion(LocalDate.now())
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .dni("87654321")
                .nombre("Maria")
                .apellido("Gomez")
                .telefono("3364111222")
                .email("maria@gmail.com")
                .direccion("Av. Siempre Viva 742")
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .fechaModificacion(LocalDate.now())
                .build();

        when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2));

        List<Cliente> resultado = useCase.listarClientes();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getDni()).isEqualTo("12345678");
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        assertThat(resultado.get(0).getApellido()).isEqualTo("Perez");
        assertThat(resultado.get(0).getTelefono()).isEqualTo("3364249176");
        assertThat(resultado.get(1).getDni()).isEqualTo("87654321");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Maria");
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay clientes")
    void debeRetornarListaVaciaSiNoHayClientes() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Cliente> resultado = useCase.listarClientes();

        assertThat(resultado).isEmpty();
        verify(clienteRepository, times(1)).findAll();
    }
}
