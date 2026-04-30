package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.ModificarMarcaCommand;
import com.taller.gestion_taller.application.command.RegistrarMarcaCommand;
import com.taller.gestion_taller.application.usecases.marca.DesactivarMarca;
import com.taller.gestion_taller.application.usecases.marca.ListarMarcas;
import com.taller.gestion_taller.application.usecases.marca.ModificarMarca;
import com.taller.gestion_taller.application.usecases.marca.RegistrarMarca;
import com.taller.gestion_taller.domain.model.Marca;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final RegistrarMarca registrarMarcaUseCase;
    private final ListarMarcas listarMarcasUseCase;
    private final ModificarMarca modificarMarcaUseCase;
    private final DesactivarMarca desactivarMarcaUseCase;

    @Transactional
    public Marca registrarMarca(RegistrarMarcaCommand command) {
        return registrarMarcaUseCase.registrarMarca(command);
    }

    @Transactional(readOnly = true)
    public List<Marca> listarMarcas() {
        return listarMarcasUseCase.listarMarcas();
    }

    @Transactional
    public Marca modificarMarca(Long id, ModificarMarcaCommand command) {
        return modificarMarcaUseCase.modificarMarca(id, command);
    }

    @Transactional
    public void desactivarMarca(Long id) {
        desactivarMarcaUseCase.desactivarMarca(id);
    }
}
