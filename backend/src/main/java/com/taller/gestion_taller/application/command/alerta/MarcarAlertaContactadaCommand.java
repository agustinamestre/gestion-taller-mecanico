package com.taller.gestion_taller.application.command.alerta;

import com.taller.gestion_taller.domain.model.MedioContacto;

public record MarcarAlertaContactadaCommand(Long alertaId, MedioContacto medioContacto, String observaciones) {}
