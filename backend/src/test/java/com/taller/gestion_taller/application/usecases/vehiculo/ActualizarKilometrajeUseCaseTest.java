package com.taller.gestion_taller.application.usecases.vehiculo;

import com.taller.gestion_taller.application.command.ActualizarKilometrajeCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarKilometrajeUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ActualizarKilometrajeUseCase actualizarKilometrajeUseCase;

    private static final Long VEHICULO_ID = 1L;
    private static final Integer KM_ACTUAL = 15000;

    private Vehiculo vehiculoExistente;

    @BeforeEach
    void setUp() {
        vehiculoExistente = Vehiculo.builder()
                .id(VEHICULO_ID)
                .patente("ABC-123")
                .anio(2020)
                .kilometrajeActual(KM_ACTUAL)
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("debe actualizar kilometraje cuando el nuevo valor es mayor al actual")
    void debeActualizarKilometrajeCuandoElNuevoValorEsMayorAlActual() {
        ActualizarKilometrajeCommand command = new ActualizarKilometrajeCommand(20000);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehiculo resultado = actualizarKilometrajeUseCase.actualizar(VEHICULO_ID, command);

        assertNotNull(resultado);
        assertEquals(20000, resultado.getKilometrajeActual());
        assertEquals("ABC-123", resultado.getPatente());
        assertEquals(2020, resultado.getAnio());

        ArgumentCaptor<Vehiculo> captor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiculoRepository).save(captor.capture());
        assertEquals(20000, captor.getValue().getKilometrajeActual());
    }

    @Test
    @DisplayName("debe permitir actualizar cuando el kilometraje es igual al actual")
    void debePermitirActualizarCuandoElKilometrajeEsIgualAlActual() {
        ActualizarKilometrajeCommand command = new ActualizarKilometrajeCommand(KM_ACTUAL);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        Vehiculo resultado = actualizarKilometrajeUseCase.actualizar(VEHICULO_ID, command);

        assertEquals(KM_ACTUAL, resultado.getKilometrajeActual());
        verify(vehiculoRepository).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando el vehiculo no existe")
    void debeLanzarNotFoundExceptionCuandoElVehiculoNoExiste() {
        ActualizarKilometrajeCommand command = new ActualizarKilometrajeCommand(20000);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> actualizarKilometrajeUseCase.actualizar(VEHICULO_ID, command));

        assertEquals("VEHICULO_NO_ENCONTRADO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("debe lanzar excepcion cuando el nuevo kilometraje es menor al actual")
    void debeLanzarExcepcionCuandoElNuevoKilometrajeEsMenorAlActual() {
        ActualizarKilometrajeCommand command = new ActualizarKilometrajeCommand(10000);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculoExistente));

        BusinessRunTimeException exception = assertThrows(BusinessRunTimeException.class,
                () -> actualizarKilometrajeUseCase.actualizar(VEHICULO_ID, command));

        assertEquals("KILOMETRAJEACTUAL_INVALIDO", exception.getBusinessError().code());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }
}
