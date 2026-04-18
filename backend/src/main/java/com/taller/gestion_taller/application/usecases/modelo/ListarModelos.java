package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.domain.model.Modelo;
import java.util.List;
import java.util.Optional;

public interface ListarModelos {
    List<Modelo> listar(Optional<Long> marcaId);
}
