package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.NotificadorCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class MarcarAlertaContactadaUseCase implements MarcarAlertaContactada {

    private final AlertaRepository alertaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final NotificadorCliente notificadorCliente;

    @Override
    public Alerta marcar(MarcarAlertaContactadaCommand command) {
        Alerta alerta = alertaRepository.findById(command.alertaId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.alertaNoEncontrada(command.alertaId())));

        alerta.marcarComoContactada(command.observaciones());

        Alerta alertaGuardada = alertaRepository.save(alerta);

        Vehiculo vehiculo = obtenerVehiculo(alertaGuardada);
        validarTelefono(vehiculo);
        notificarPorEmailSiCorresponde(vehiculo);

        return alertaGuardada;
    }

    private void notificarPorEmailSiCorresponde(Vehiculo vehiculo) {
        String email = vehiculo.getCliente().getEmail();
        if (StringUtils.hasText(email)) {
            notificadorCliente.notificarPorEmail(email, nombreCompleto(vehiculo), vehiculo.getPatente());
        }
    }

    private void validarTelefono(Vehiculo vehiculo) {
        String telefono = vehiculo.getCliente().getTelefono();
        if (!StringUtils.hasText(telefono)) {
            throw new BusinessRunTimeException(BusinessErrors.clienteSinTelefono());
        }
    }

    private Vehiculo obtenerVehiculo(Alerta alerta) {
        return vehiculoRepository.findById(alerta.getVehiculoId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.vehiculoNoEncontrado(alerta.getVehiculoId())));
    }

    private String nombreCompleto(Vehiculo vehiculo) {
        return (vehiculo.getCliente().getNombre() + " " + vehiculo.getCliente().getApellido()).trim();
    }
}
