package com.taller.gestion_taller.infrastructure.jobs;

import com.taller.gestion_taller.infrastructure.service.PresupuestoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VencimientoPresupuestoJob {

    private final PresupuestoService presupuestoService;

    @Scheduled(cron = "0 0 2 * * ?") // 2 am
    public void ejecutar() {
        int cantidad = presupuestoService.marcarPresupuestosVencidos();
        log.info("Job de vencimiento de presupuestos ejecutado. Presupuestos marcados como VENCIDO: {}", cantidad);
    }
}
