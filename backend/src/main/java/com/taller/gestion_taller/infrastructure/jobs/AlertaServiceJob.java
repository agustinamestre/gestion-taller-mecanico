package com.taller.gestion_taller.infrastructure.jobs;

import com.taller.gestion_taller.infrastructure.service.AlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaServiceJob {

    private final AlertaService alertaService;

    @Scheduled(cron = "0 0 4 * * MON") // lunes 4 am
    public void ejecutar() {
        int cantidad = alertaService.generarAlertasService();
        log.info("Job de alertas de service ejecutado. Alertas generadas: {}", cantidad);
    }
}
