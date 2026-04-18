package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.application.command.ModificarModeloCommand;
import com.taller.gestion_taller.domain.model.Modelo;

public interface ModificarModelo {
    Modelo modificar(Long id, ModificarModeloCommand command);
}
