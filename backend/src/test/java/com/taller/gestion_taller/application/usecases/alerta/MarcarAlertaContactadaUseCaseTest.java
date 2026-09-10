package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.MedioContacto;
import com.taller.gestion_taller.domain.model.TipoAlerta;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarcarAlertaContactadaUseCaseTest {

    private static final Long ALERTA_ID = 1L;

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private MarcarAlertaContactadaUseCase useCase;

    private static Alerta alertaPendiente() {
        return Alerta.builder()
                .id(ALERTA_ID)
                .vehiculoId(5L)
                .tipo(TipoAlerta.SERVICE_VENCIDO)
                .fechaAlerta(LocalDate.now())
                .contactado(false)
                .build();
    }

    @Test
    @DisplayName("Debe marcar la alerta como contactada con fecha y medio correctos")
    void debeMarcarAlertaComoContactadaCorrectamente() {
        Alerta alerta = alertaPendiente();
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, MedioContacto.WHATSAPP, "Cliente confirmo turno");

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(alerta)).thenReturn(alerta);

        Alerta resultado = useCase.marcar(command);

        assertThat(resultado.isContactado()).isTrue();
        assertThat(resultado.getFechaContacto()).isEqualTo(LocalDate.now());
        assertThat(resultado.getMedioContacto()).isEqualTo(MedioContacto.WHATSAPP);
        assertThat(resultado.getObservaciones()).isEqualTo("Cliente confirmo turno");
        verify(alertaRepository).save(alerta);
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando la alerta no existe")
    void debeLanzarNotFoundExceptionCuandoLaAlertaNoExiste() {
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, MedioContacto.EMAIL, null);

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> useCase.marcar(command));

        assertEquals("ALERTA_NO_ENCONTRADA", exception.getBusinessError().code());
        verify(alertaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando la alerta ya fue contactada")
    void debeLanzarExcepcionCuandoLaAlertaYaFueContactada() {
        Alerta alertaYaContactada = alertaPendiente().toBuilder()
                .contactado(true)
                .fechaContacto(LocalDate.now().minusDays(1))
                .medioContacto(MedioContacto.EMAIL)
                .build();

        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, MedioContacto.WHATSAPP, null);

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alertaYaContactada));

        BusinessRunTimeException exception =
                assertThrows(BusinessRunTimeException.class, () -> useCase.marcar(command));

        assertEquals("ALERTA_YA_CONTACTADA", exception.getBusinessError().code());
        verify(alertaRepository, never()).save(any());
    }
}
