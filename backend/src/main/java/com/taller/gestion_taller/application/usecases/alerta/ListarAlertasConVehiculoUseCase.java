package com.taller.gestion_taller.application.usecases.alerta;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.model.Vehiculo;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarAlertasConVehiculoUseCase implements ListarAlertasConVehiculo {

    private final AlertaRepository alertaRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public List<AlertaConVehiculo> listar(String patente, Boolean contactado) {
        List<Alerta> alertas = alertaRepository.findByFiltros(patente, contactado);

        List<Long> vehiculoIds = alertas.stream()
                .map(Alerta::getVehiculoId)
                .distinct()
                .toList();

        Map<Long, Vehiculo> vehiculosPorId = vehiculoRepository.findByIdIn(vehiculoIds).stream()
                .collect(Collectors.toMap(Vehiculo::getId, Function.identity()));

        return alertas.stream()
                .map(alerta -> new AlertaConVehiculo(alerta, obtenerVehiculo(alerta, vehiculosPorId)))
                .toList();
    }

    private Vehiculo obtenerVehiculo(Alerta alerta, Map<Long, Vehiculo> vehiculosPorId) {
        Vehiculo vehiculo = vehiculosPorId.get(alerta.getVehiculoId());
        if (vehiculo == null) {
            throw new NotFoundException(BusinessErrors.vehiculoNoEncontrado(alerta.getVehiculoId()));
        }
        return vehiculo;
    }
}
