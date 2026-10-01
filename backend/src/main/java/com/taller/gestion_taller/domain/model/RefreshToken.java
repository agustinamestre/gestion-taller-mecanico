package com.taller.gestion_taller.domain.model;

import java.time.Instant;

public class RefreshToken {

    private Long id;
    private final Long usuarioId;
    private final String tokenHash;
    private final Instant fechaExpiracion;
    private boolean revocado;

    private RefreshToken(Long id, Long usuarioId, String tokenHash, Instant fechaExpiracion, boolean revocado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tokenHash = tokenHash;
        this.fechaExpiracion = fechaExpiracion;
        this.revocado = revocado;
    }

    public static RefreshToken crearNuevo(Long usuarioId, String tokenHash, Instant fechaExpiracion) {
        return new RefreshToken(null, usuarioId, tokenHash, fechaExpiracion, false);
    }

    public static RefreshToken reconstruir(Long id, Long usuarioId, String tokenHash,
                                           Instant fechaExpiracion, boolean revocado) {
        return new RefreshToken(id, usuarioId, tokenHash, fechaExpiracion, revocado);
    }

    public boolean esValido() {
        return !revocado && fechaExpiracion.isAfter(Instant.now());
    }

    public void revocar() {
        this.revocado = true;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getTokenHash() { return tokenHash; }
    public Instant getFechaExpiracion() { return fechaExpiracion; }
    public boolean isRevocado() { return revocado; }
}