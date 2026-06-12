package com.taller.gestion_taller.domain.model;

public enum EstadoOrdenTrabajo {
    INGRESADO {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return nuevo == EN_REPARACION || nuevo == CANCELADO;
        }

        @Override
        public boolean esModificable() {
            return true;
        }
    },
    EN_REPARACION {
        @Override
        public boolean puedeTransicionarA(EstadoOrdenTrabajo nuevo) {
            return nuevo == FINALIZADO;
        }

        @Override
        public boolean esModificable() {
            return true;
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

    public boolean esModificable() {
        return false;
    }
}
