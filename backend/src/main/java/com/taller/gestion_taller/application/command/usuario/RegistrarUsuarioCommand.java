package com.taller.gestion_taller.application.command.usuario;

import com.taller.gestion_taller.domain.model.Rol;

public record RegistrarUsuarioCommand(
        String username,
        String password,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        Rol rol
) {}