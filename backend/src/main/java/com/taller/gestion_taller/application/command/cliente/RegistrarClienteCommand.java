package com.taller.gestion_taller.application.command.cliente;

import com.taller.gestion_taller.domain.model.SituacionIva;

public record RegistrarClienteCommand(
        String dni,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        SituacionIva situacionIva

) {}