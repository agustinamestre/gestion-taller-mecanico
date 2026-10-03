package com.taller.gestion_taller.domain.service;

public interface NotificadorCliente {

    void notificarPorEmail(String email, String nombreCliente, String patenteVehiculo);
}
