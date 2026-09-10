package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarcarAlertaContactadaUseCase implements MarcarAlertaContactada {

    private final AlertaRepository alertaRepository;

    @Override
    public Alerta marcar(MarcarAlertaContactadaCommand command) {
        Alerta alerta = alertaRepository.findById(command.alertaId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.alertaNoEncontrada(command.alertaId())));

        alerta.marcarComoContactada(command.medioContacto(), command.observaciones());

        return alertaRepository.save(alerta);
    }
}
