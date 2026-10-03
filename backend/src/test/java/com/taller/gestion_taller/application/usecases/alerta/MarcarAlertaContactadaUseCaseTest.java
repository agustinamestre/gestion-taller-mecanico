package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.model.MedioContacto;
import com.taller.gestion_taller.domain.model.TipoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.NotificadorCliente;
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
    private static final Long VEHICULO_ID = 5L;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private NotificadorCliente notificadorCliente;

    @InjectMocks
    private MarcarAlertaContactadaUseCase useCase;

    private static Alerta alertaPendiente() {
        return Alerta.builder()
                .id(ALERTA_ID)
                .vehiculoId(VEHICULO_ID)
                .tipo(TipoAlerta.SERVICE_VENCIDO)
                .fechaAlerta(LocalDate.now())
                .contactado(false)
                .build();
    }

    private static Vehiculo vehiculoConCliente(String email, String telefono) {
        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email(email)
                .telefono(telefono)
                .build();

        return Vehiculo.builder()
                .id(VEHICULO_ID)
                .patente("AA123BB")
                .cliente(cliente)
                .build();
    }

    @Test
    @DisplayName("Debe marcar la alerta como contactada por WhatsApp y notificar por email si el cliente tiene uno")
    void debeMarcarAlertaComoContactadaYNotificarPorEmailSiCorresponde() {
        Alerta alerta = alertaPendiente();
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, "Cliente confirmo turno");

        Vehiculo vehiculo = vehiculoConCliente("cliente@mail.com", "+541123456789");

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(alerta)).thenReturn(alerta);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculo));

        Alerta resultado = useCase.marcar(command);

        assertThat(resultado.isContactado()).isTrue();
        assertThat(resultado.getFechaContacto()).isEqualTo(LocalDate.now());
        assertThat(resultado.getMedioContacto()).isEqualTo(MedioContacto.WHATSAPP);
        assertThat(resultado.getObservaciones()).isEqualTo("Cliente confirmo turno");
        verify(alertaRepository).save(alerta);
        verify(notificadorCliente).notificarPorEmail("cliente@mail.com", "Juan Perez", "AA123BB");
    }

    @Test
    @DisplayName("No debe notificar por email si el cliente no tiene uno cargado")
    void noDebeNotificarPorEmailSiElClienteNoTieneUnoCargado() {
        Alerta alerta = alertaPendiente();
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, null);

        Vehiculo vehiculo = vehiculoConCliente(null, "+541123456789");

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(alerta)).thenReturn(alerta);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculo));

        useCase.marcar(command);

        verify(notificadorCliente, never()).notificarPorEmail(any(), any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepcion si el cliente no tiene telefono, ya que el contacto siempre es por WhatsApp")
    void debeLanzarExcepcionSiNoTieneTelefono() {
        Alerta alerta = alertaPendiente();
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, null);

        Vehiculo vehiculo = vehiculoConCliente("cliente@mail.com", null);

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(alerta)).thenReturn(alerta);
        when(vehiculoRepository.findById(VEHICULO_ID)).thenReturn(Optional.of(vehiculo));

        BusinessRunTimeException exception =
                assertThrows(BusinessRunTimeException.class, () -> useCase.marcar(command));

        assertEquals("CLIENTE_SIN_TELEFONO", exception.getBusinessError().code());
    }

    @Test
    @DisplayName("Debe lanzar NotFoundException cuando la alerta no existe")
    void debeLanzarNotFoundExceptionCuandoLaAlertaNoExiste() {
        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, null);

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
                .medioContacto(MedioContacto.WHATSAPP)
                .build();

        MarcarAlertaContactadaCommand command =
                new MarcarAlertaContactadaCommand(ALERTA_ID, null);

        when(alertaRepository.findById(ALERTA_ID)).thenReturn(Optional.of(alertaYaContactada));

        BusinessRunTimeException exception =
                assertThrows(BusinessRunTimeException.class, () -> useCase.marcar(command));

        assertEquals("ALERTA_YA_CONTACTADA", exception.getBusinessError().code());
        verify(alertaRepository, never()).save(any());
    }
}
