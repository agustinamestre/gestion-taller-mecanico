package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContarAlertasPendientesUseCase implements ContarAlertasPendientes {

    private final AlertaRepository alertaRepository;

    @Override
    public long contar() {
        return alertaRepository.countByContactadoFalse();
    }
}
