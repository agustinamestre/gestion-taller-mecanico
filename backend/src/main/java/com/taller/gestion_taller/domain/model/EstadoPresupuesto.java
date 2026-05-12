package com.taller.gestion_taller.domain.model;

public enum EstadoPresupuesto {
    PENDIENTE {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return nuevo == APROBADO || nuevo == RECHAZADO || nuevo == VENCIDO;
        }
    },
    APROBADO {
        @Override
        public boolean puedeTransicionarA(EstadoPresupuesto nuevo) {
            return false;
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
    };

    public abstract boolean puedeTransicionarA(EstadoPresupuesto nuevo);
}

