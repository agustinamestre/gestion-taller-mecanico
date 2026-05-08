package com.taller.gestion_taller.application.command.cliente;

public record RegistrarClienteCommand(
        String dni,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion
) {}