package com.taller.gestion_taller.domain.model;

public enum EstadoFactura {
    EMITIDA {
        @Override
        public boolean puedeTransicionarA(EstadoFactura nuevo) {
            return nuevo == ANULADA;
        }
    },
    ANULADA {
        @Override
        public boolean puedeTransicionarA(EstadoFactura nuevo) {
            return false;
        }
    };

    public abstract boolean puedeTransicionarA(EstadoFactura nuevo);
}
