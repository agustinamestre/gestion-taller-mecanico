package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetVehiculoByPatenteUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private GetVehiculoByPatenteUseCase getVehiculoByPatenteUseCase;

    @Test
    public void testGetByPatente_shouldReturnVehiculo_whenPatenteExists() {
        String patente = "ABC123";
        Vehiculo vehiculo = Vehiculo.builder().patente(patente).build();

        when(vehiculoRepository.findByPatente(patente)).thenReturn(Optional.of(vehiculo));

        Vehiculo result = getVehiculoByPatenteUseCase.getByPatente(patente);

        assertNotNull(result);
        assertEquals(patente, result.getPatente());
    }

    @Test
    public void testGetByPatente_shouldThrowNotFoundException_whenPatenteDoesNotExist() {
        String patente = "ABC123";

        when(vehiculoRepository.findByPatente(patente)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            getVehiculoByPatenteUseCase.getByPatente(patente);
        });
    }
}
