package com.taller.gestion_taller.infrastructure.rest.dto;

public record ModeloResponse (
         Long id,
         String nombre,
         boolean activo,
         MarcaResponse marca
) { }
