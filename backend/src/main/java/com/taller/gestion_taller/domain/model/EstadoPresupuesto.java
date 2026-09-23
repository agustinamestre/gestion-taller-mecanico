package com.taller.gestion_taller.domain.model;

public enum EstadoPresupuesto {
    PENDIENTE {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return nuevo == APROBADO || nuevo == RECHAZADO;
        }
    },
    APROBADO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return nuevo == VENCIDO;
        }
    },
    RECHAZADO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return false;
        }
    },
    VENCIDO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return false;
        }
    },
    UTILIZADO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return false;
        }
    },
    CANCELADO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return false;
        }
    };

    public abstract boolean puedeTransicionarA(EstadoPresupuesto nuevo);
}