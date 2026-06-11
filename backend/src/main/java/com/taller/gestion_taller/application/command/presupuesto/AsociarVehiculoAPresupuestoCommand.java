package com.taller.gestion_taller.application.command.presupuesto;

public record AsociarVehiculoAPresupuestoCommand(
        Long presupuestoId,
        Long vehiculoId,
        DatosVehiculoNuevo datosVehiculoNuevo,
        Long clienteId,
        DatosClienteNuevo datosClienteNuevo
) {

    public boolean tieneVehiculoExistente() {
        return vehiculoId != null;
    }

    public boolean tieneClienteExistente() {
        return clienteId != null;
    }

    public record DatosVehiculoNuevo(
            String patente,
            Long modeloId,
            Integer anio,
            Integer kilometrajeActual
    ) {}

    public record DatosClienteNuevo(
            String dni,
            String nombre,
            String apellido,
            String telefono,
            String email,
            String direccion
    ) {}
}
