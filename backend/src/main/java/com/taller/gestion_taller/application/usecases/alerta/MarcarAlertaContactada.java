package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.domain.model.Alerta;

public interface MarcarAlertaContactada {
    Alerta marcar(MarcarAlertaContactadaCommand command);
}
