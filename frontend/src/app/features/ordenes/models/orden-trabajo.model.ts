import { ItemPresupuestoResponse } from '../../presupuestos/models/presupuesto.model';

export type EstadoOrdenTrabajo = 'INGRESADO' | 'EN_REPARACION' | 'FINALIZADO' | 'ENTREGADO' | 'CANCELADO';

export interface ItemOrdenTrabajoResponse {
  id: number;
  productoId: number;
  tipo: string;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface OrdenTrabajoResponse {
  id: number;
  patenteVehiculo: string;
  presupuestoId: number | null;
  fechaIngreso: string;
  fechaEgreso: string | null;
  descripcionProblema: string;
  estado: EstadoOrdenTrabajo;
  usuarioCreacionId: number;
  itemsPresupuesto: ItemPresupuestoResponse[];
  itemsOrden: ItemOrdenTrabajoResponse[];
  total: number;
}

export interface RegistrarOrdenTrabajoRequest {
  patente?: string;
  presupuestoId?: number;
  descripcionProblema?: string;
  usuarioCreacionId: number;
}

export interface ModificarOrdenTrabajoRequest {
  descripcionProblema: string;
}

export interface AgregarItemOrdenTrabajoRequest {
  productoId: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

export interface ModificarItemOrdenTrabajoRequest {
  productoId: number | null;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

export interface CambiarEstadoOrdenTrabajoRequest {
  nuevoEstado: EstadoOrdenTrabajo;
}

export const TRANSICIONES_VALIDAS_ORDEN: Record<EstadoOrdenTrabajo, EstadoOrdenTrabajo[]> = {
  INGRESADO: ['EN_REPARACION', 'CANCELADO'],
  EN_REPARACION: ['FINALIZADO'],
  FINALIZADO: ['ENTREGADO'],
  ENTREGADO: [],
  CANCELADO: [],
};

export const ESTADOS_MODIFICABLES: EstadoOrdenTrabajo[] = ['INGRESADO', 'EN_REPARACION'];