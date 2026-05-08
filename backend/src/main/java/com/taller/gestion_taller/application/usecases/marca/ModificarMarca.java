package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.application.command.marca.ModificarMarcaCommand;
import com.taller.gestion_taller.domain.model.Marca;

public interface ModificarMarca {
    Marca modificarMarca(Long id, ModificarMarcaCommand command);

}
