package com.taller.gestion_taller.domain.model;

public enum EstadoOrdenTrabajo {
    INGRESADO {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return nuevo == EN_REPARACION || nuevo == CANCELADO;
        }
    },
    EN_REPARACION {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return nuevo == FINALIZADO;
        }
    },
    FINALIZADO {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return nuevo == ENTREGADO;
        }
    },
    ENTREGADO {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return false;
        }
    },
    CANCELADO {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return false;
        }
    };

    public abstract boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo);
}
