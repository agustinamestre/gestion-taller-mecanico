package com.taller.gestion_taller.infrastructure.rest.dto.usuario.response;

import com.taller.gestion_taller.domain.model.Rol;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        String apellido,
        String telefono,
        String email,
        String direccion,
        Rol rol,
        boolean activo
) {}