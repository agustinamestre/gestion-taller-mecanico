package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarVehiculosUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ListarVehiculosUseCase useCase;

    @Test
    @DisplayName("Debe retornar todos los vehiculos activos cuando no se indica patente")
    void debeRetornarTodosLosVehiculosActivosCuandoPatenteEsNull() {
        Vehiculo vehiculo1 = Vehiculo.builder().id(1L).patente("ABC123").build();
        Vehiculo vehiculo2 = Vehiculo.builder().id(2L).patente("XYZ789").build();

        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of(vehiculo1, vehiculo2));

        List<Vehiculo> resultado = useCase.listar(null);

        assertEquals(List.of(vehiculo1, vehiculo2), resultado);
        verify(vehiculoRepository, never()).findByPatenteContainingAndActivoTrue(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Debe retornar todos los vehiculos activos cuando la patente esta en blanco")
    void debeRetornarTodosLosVehiculosActivosCuandoPatenteEstaEnBlanco() {
        when(vehiculoRepository.findByActivoTrue()).thenReturn(List.of());

        List<Vehiculo> resultado = useCase.listar("   ");

        assertEquals(List.of(), resultado);
        verify(vehiculoRepository, never()).findByPatenteContainingAndActivoTrue(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Debe retornar los vehiculos activos que coincidan con la patente indicada")
    void debeRetornarVehiculosFiltradosPorPatente() {
        Vehiculo vehiculo = Vehiculo.builder().id(1L).patente("ABC123").build();

        when(vehiculoRepository.findByPatenteContainingAndActivoTrue("ABC")).thenReturn(List.of(vehiculo));

        List<Vehiculo> resultado = useCase.listar("ABC");

        assertEquals(List.of(vehiculo), resultado);
        verify(vehiculoRepository, never()).findByActivoTrue();
    }
}
