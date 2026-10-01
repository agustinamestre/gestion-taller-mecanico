package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
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
@DisplayName("ReactivarVehiculoUseCase")
class ReactivarVehiculoUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ReactivarVehiculoUseCase useCase;

    @Test
    @DisplayName("Reactivar vehiculo exitosamente")
    void debeReactivarVehiculoExitosamente() {
        Long id = 1L;
        Vehiculo vehiculo = mock(Vehiculo.class);

        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(vehiculo));
        when(vehiculo.reactivar()).thenReturn(vehiculo);
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);

        useCase.reactivarVehiculo(id);

        verify(vehiculoRepository).findById(id);
        verify(vehiculo).reactivar();
        verify(vehiculoRepository).save(vehiculo);
    }

    @Test
    @DisplayName("Lanzar excepción cuando el vehiculo a reactivar no existe")
    void debeLanzarExcepcionCuandoVehiculoNoExiste() {
        Long id = 1L;

        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            useCase.reactivarVehiculo(id);
        });

        assertTrue(exception.getMessage().contains("No se encontro el vehiculo ingresado."));
        verify(vehiculoRepository, never()).save(any());
    }
}
