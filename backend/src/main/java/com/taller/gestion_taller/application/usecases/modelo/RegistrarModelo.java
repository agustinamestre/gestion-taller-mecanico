package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.domain.model.Modelo;

public interface RegistrarModelo {
    Modelo registrar(RegistrarModeloCommand command);
}
