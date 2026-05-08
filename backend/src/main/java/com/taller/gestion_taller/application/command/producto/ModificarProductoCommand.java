package com.taller.gestion_taller.application.command.producto;

public record ModificarProductoCommand(
        String nombre,
        String descripcion,
        String tipo)
{ }
