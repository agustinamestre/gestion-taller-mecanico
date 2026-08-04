package com.taller.gestion_taller.application.command.cliente;

import com.taller.gestion_taller.domain.model.SituacionIva;

public record ModificarClienteCommand(
        String nroDocumento,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        SituacionIva situacionIva
) {}
