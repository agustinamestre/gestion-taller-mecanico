package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.MotivoAlerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import com.taller.gestion_taller.domain.service.PoliticaServiceVehiculo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class GenerarAlertasServiceUseCase implements GenerarAlertasService {

    private final VehiculoRepository vehiculoRepository;
    private final AlertaRepository alertaRepository;
    private final PoliticaServiceVehiculo politicaServiceVehiculo;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    @Override
    public int generar() {
        LocalDate hoy = LocalDate.now();

        int generadas = 0;
        for (Vehiculo vehiculo : vehiculoRepository.findByActivoTrue()) {
            Optional<MotivoAlerta> motivo = politicaServiceVehiculo.evaluar(vehiculo, hoy);
            if (motivo.isEmpty()) {
                continue;
            }

            // si esta en el taller haciendo el service entonces se registra al entregar la orden.
            if (ordenTrabajoRepository.existsOrdenEnCursoConService(vehiculo.getId())) {
                continue;
            }

            boolean existeAlertaVigente = alertaRepository.existsAlertaVigentePorVehiculo(
                    vehiculo.getId(), vehiculo.fechaReferenciaService());

            if (!existeAlertaVigente) {
                alertaRepository.save(Alerta.crearPorServiceVencido(vehiculo.getId(), motivo.get()));
                generadas++;
            }
        }

        return generadas;
    }
}
