package com.taller.gestion_taller.application.command;

public record ModificarClienteCommand(
        String nroDocumento,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion
) {}
