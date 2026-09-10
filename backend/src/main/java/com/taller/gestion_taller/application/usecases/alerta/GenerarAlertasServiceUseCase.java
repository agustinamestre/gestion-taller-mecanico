package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class GenerarAlertasServiceUseCase implements GenerarAlertasService {

    private final VehiculoRepository vehiculoRepository;
    private final AlertaRepository alertaRepository;

    @Override
    public int generar() {
        LocalDate umbral = LocalDate.now().minusMonths(12);
        List<Vehiculo> vehiculosVencidos = vehiculoRepository.findByActivoTrueAndFechaUltimoServiceBefore(umbral);

        int generadas = 0;
        for (Vehiculo vehiculo : vehiculosVencidos) {
            boolean existeAlertaVigente = alertaRepository.existsAlertaVigentePorVehiculo(
                    vehiculo.getId(), vehiculo.getFechaUltimoService());

            if (!existeAlertaVigente) {
                alertaRepository.save(Alerta.crearPorServiceVencido(vehiculo.getId()));
                generadas++;
            }
        }

        return generadas;
    }
}
