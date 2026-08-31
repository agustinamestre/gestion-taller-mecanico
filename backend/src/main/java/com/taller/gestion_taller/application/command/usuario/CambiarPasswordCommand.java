package com.taller.gestion_taller.application.command.usuario;

public record CambiarPasswordCommand(
        String username,
        String passwordActual,
        String passwordNueva
) {}