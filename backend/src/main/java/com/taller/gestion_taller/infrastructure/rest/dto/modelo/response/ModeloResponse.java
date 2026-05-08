package com.taller.gestion_taller.infrastructure.rest.dto.modelo.response;

import com.taller.gestion_taller.infrastructure.rest.dto.marca.response.MarcaResponse;

public record ModeloResponse (
         Long id,
         String nombre,
         boolean activo,
         MarcaResponse marca
) { }
