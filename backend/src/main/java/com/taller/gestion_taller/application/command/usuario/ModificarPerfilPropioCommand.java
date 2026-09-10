package com.taller.gestion_taller.application.command.usuario;

public record ModificarPerfilPropioCommand(
        String username,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion
) {}
