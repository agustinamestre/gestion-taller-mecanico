package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ListarClienteUseCase useCase;

    private Cliente clienteBase(String dni) {
        return Cliente.builder()
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
    }

    @Test
    @DisplayName("Debe retornar cliente sin vehículos")
    void debeRetornarClienteSinVehiculosCuandoNoPosee() {
        String dni = "12345678";
        Cliente clienteEsperado = clienteBase(dni);

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(clienteEsperado));
        when(vehiculoRepository.findByClienteId(clienteEsperado.getId())).thenReturn(Collections.emptyList());

        Cliente resultado = useCase.listarCliente(dni);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getDni()).isEqualTo(dni);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Perez");
        assertThat(resultado.getTelefono()).isEqualTo("3364249176");
        assertThat(resultado.getEmail()).isEqualTo("juan@gmail.com");
        assertThat(resultado.getDireccion()).isEqualTo("Calle Falsa 123");
        assertThat(resultado.isActivo()).isTrue();
        assertThat(resultado.getVehiculos()).isNotNull().isEmpty();

        verify(clienteRepository, times(1)).findByDni(dni);
        verify(vehiculoRepository, times(1)).findByClienteId(clienteEsperado.getId());
    }

    @Test
    @DisplayName("Debe retornar cliente con todos sus vehículos asociados")
    void debeRetornarClienteConVehiculosAsociados() {
        String dni = "12345678";
        Cliente clienteEsperado = clienteBase(dni);

        Marca toyota = Marca.builder().id(1L).nombre("Toyota").activo(true).build();
        Modelo corolla = Modelo.builder().id(1L).nombre("Corolla").marca(toyota).activo(true).build();

        Vehiculo vehiculo1 = Vehiculo.builder()
                .id(10L)
                .patente("AE123FJ")
                .modelo(corolla)
                .anio(2021)
                .kilometrajeActual(55000)
                .activo(true)
                .build();

        Vehiculo vehiculo2 = Vehiculo.builder()
                .id(11L)
                .patente("AE123FP")
                .modelo(corolla)
                .anio(2022)
                .kilometrajeActual(30000)
                .activo(true)
                .build();

        List<Vehiculo> vehiculos = List.of(vehiculo1, vehiculo2);

        when(clienteRepository.findByDni(dni)).thenReturn(Optional.of(clienteEsperado));
        when(vehiculoRepository.findByClienteId(clienteEsperado.getId())).thenReturn(vehiculos);

        Cliente resultado = useCase.listarCliente(dni);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getDni()).isEqualTo(dni);
        assertThat(resultado.getVehiculos())
                .isNotNull()
                .hasSize(2)
                .extracting(Vehiculo::getPatente)
                .containsExactly("AE123FJ", "AE123FP");

        assertThat(resultado.getVehiculos().get(0).getModelo().getNombre()).isEqualTo("Corolla");
        assertThat(resultado.getVehiculos().get(0).getModelo().getMarca().getNombre()).isEqualTo("Toyota");
        assertThat(resultado.getVehiculos().get(0).getAnio()).isEqualTo(2021);

        verify(clienteRepository, times(1)).findByDni(dni);
        verify(vehiculoRepository, times(1)).findByClienteId(clienteEsperado.getId());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando no existe cliente con el DNI")
    void debeLanzarExcepcionCuandoNoExiste() {
        String dni = "99999999";
        when(clienteRepository.findByDni(dni)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> useCase.listarCliente(dni));

        assertTrue(exception.getMessage().contains("No se encontro un cliente con DNI: 99999999"));
        verify(clienteRepository, times(1)).findByDni(dni);
        verify(vehiculoRepository, never()).findByClienteId(anyLong());
    }
}
