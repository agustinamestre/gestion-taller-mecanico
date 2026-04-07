package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;

public interface RegistrarMarca {
    Marca registrarMarca(RegistrarMarcaCommand command);
}
