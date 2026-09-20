package com.taller.gestion_taller.application.usecases.presupuesto;

import com.taller.gestion_taller.domain.model.Presupuesto;
import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isAllBlank;

@RequiredArgsConstructor
public class ListarPresupuestosUseCase implements ListarPresupuestos {

    private final PresupuestoRepository presupuestoRepository;

    @Override
    public List<Presupuesto> listar(String patente, String dni) {

        boolean sinFiltros = isAllBlank(patente, dni);

        if (sinFiltros) {
            return presupuestoRepository.findAll();
        }
        return presupuestoRepository.buscar(patente, dni);
    }
}
