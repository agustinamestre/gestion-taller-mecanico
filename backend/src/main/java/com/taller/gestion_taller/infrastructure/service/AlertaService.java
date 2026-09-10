package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.application.command.alerta.MarcarAlertaContactadaCommand;
import com.taller.gestion_taller.application.usecases.alerta.AlertaConVehiculo;
import com.taller.gestion_taller.application.usecases.alerta.ContarAlertasPendientes;
import com.taller.gestion_taller.application.usecases.alerta.GenerarAlertasService;
import com.taller.gestion_taller.application.usecases.alerta.ListarAlertasConVehiculo;
import com.taller.gestion_taller.application.usecases.alerta.MarcarAlertaContactada;
import com.taller.gestion_taller.application.usecases.vehiculo.ObtenerVehiculoPorId;
import com.taller.gestion_taller.domain.model.Alerta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final GenerarAlertasService generarAlertasServiceUseCase;
    private final MarcarAlertaContactada marcarAlertaContactadaUseCase;
    private final ListarAlertasConVehiculo listarAlertasConVehiculoUseCase;
    private final ContarAlertasPendientes contarAlertasPendientesUseCase;
    private final ObtenerVehiculoPorId obtenerVehiculoPorIdUseCase;

    @Transactional
    public int generarAlertasService() {
        return generarAlertasServiceUseCase.generar();
    }

    @Transactional
    public AlertaConVehiculo marcarContactada(MarcarAlertaContactadaCommand command) {
        Alerta alerta = marcarAlertaContactadaUseCase.marcar(command);
        return new AlertaConVehiculo(alerta, obtenerVehiculoPorIdUseCase.obtener(alerta.getVehiculoId()));
    }

    @Transactional(readOnly = true)
    public List<AlertaConVehiculo> listarConVehiculo(String patente, Boolean contactado) {
        return listarAlertasConVehiculoUseCase.listar(patente, contactado);
    }

    @Transactional(readOnly = true)
    public long contarPendientes() {
        return contarAlertasPendientesUseCase.contar();
    }
}
