package com.taller.gestion_taller.application.command;

public record ModificarProductoCommand(
        String nombre,
        String descripcion,
        String tipo)
{ }
