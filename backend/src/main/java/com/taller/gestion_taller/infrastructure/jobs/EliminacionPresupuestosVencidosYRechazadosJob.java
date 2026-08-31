package com.taller.gestion_taller.infrastructure.jobs;

import com.taller.gestion_taller.infrastructure.service.PresupuestoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EliminacionPresupuestosVencidosYRechazadosJob {

    private final PresupuestoService presupuestoService;

    @Scheduled(cron = "0 0 3 * * ?") // 3 am
    public void ejecutar() {
        int cantidad = presupuestoService.eliminarPresupuestosVencidosYRechazados();
        log.info("Job de eliminacion de presupuestos ejecutado. Presupuestos eliminados (VENCIDO/RECHAZADO, +180 dias): {}", cantidad);
    }
}
