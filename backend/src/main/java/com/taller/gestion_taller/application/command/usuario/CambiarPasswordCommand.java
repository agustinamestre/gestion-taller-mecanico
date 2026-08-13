package com.taller.gestion_taller.application.command.usuario;

public record CambiarPasswordCommand(
        Long id,
        String passwordActual,
        String passwordNueva
) {}