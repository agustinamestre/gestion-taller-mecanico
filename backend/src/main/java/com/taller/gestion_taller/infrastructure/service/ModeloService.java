package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.ModificarModeloCommand;
import com.taller.gestion_taller.application.command.RegistrarModeloCommand;
import com.taller.gestion_taller.application.usecases.modelo.DesactivarModelo;
import com.taller.gestion_taller.application.usecases.modelo.ListarModelos;
import com.taller.gestion_taller.application.usecases.modelo.ModificarModelo;
import com.taller.gestion_taller.application.usecases.modelo.RegistrarModelo;
import com.taller.gestion_taller.domain.model.Modelo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModeloService {

    private final RegistrarModelo registrarModeloUseCase;
    private final ListarModelos listarModelosUseCase;
    private final ModificarModelo modificarModeloUseCase;
    private final DesactivarModelo desactivarModeloUseCase;

    @Transactional
    public Modelo registrarModelo(RegistrarModeloCommand command) {
        return registrarModeloUseCase.registrar(command);
    }

    @Transactional(readOnly = true)
    public List<Modelo> listarModelos(Optional<Long> marcaId) {
        return listarModelosUseCase.listar(marcaId);
    }

    @Transactional
    public Modelo modificarModelo(Long id, ModificarModeloCommand command) {
        return modificarModeloUseCase.modificar(id, command);
    }

    @Transactional
    public void desactivarModelo(Long id) {
        desactivarModeloUseCase.desactivar(id);
    }
}
