package com.taller.gestion_taller.infrastructure.rest.dto;

public record VehiculoResponse(
        Long id,
        String patente,
        Integer anio,
        Integer kilometrajeActual,
        VehiculoModeloResponse modelo,
        VehiculoClienteResponse cliente
) {
    public record VehiculoModeloResponse(String nombre, VehiculoMarcaResponse marca) {}
    public record VehiculoMarcaResponse(String nombre) {}
    public record VehiculoClienteResponse(String dni, String nombre, String apellido) {}
}
