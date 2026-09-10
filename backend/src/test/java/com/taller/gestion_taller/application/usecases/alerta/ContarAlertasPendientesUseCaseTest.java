package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContarAlertasPendientesUseCaseTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private ContarAlertasPendientesUseCase useCase;

    @Test
    @DisplayName("Debe devolver la cantidad de alertas pendientes de contactar")
    void debeDevolverLaCantidadDeAlertasPendientes() {
        when(alertaRepository.countByContactadoFalse()).thenReturn(3L);

        long resultado = useCase.contar();

        assertThat(resultado).isEqualTo(3L);
    }

    @Test
    @DisplayName("Debe devolver 0 cuando no hay alertas pendientes")
    void debeDevolverCeroCuandoNoHayAlertasPendientes() {
        when(alertaRepository.countByContactadoFalse()).thenReturn(0L);

        long resultado = useCase.contar();

        assertThat(resultado).isEqualTo(0L);
    }
}
