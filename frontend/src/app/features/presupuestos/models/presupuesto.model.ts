import { SituacionIva } from "../../clientes/models/cliente.model";

export type EstadoPresupuesto = 'PENDIENTE' | 'APROBADO' | 'RECHAZADO' | 'VENCIDO';

export interface ItemPresupuestoResponse {
  id: number;
  presupuestoId: number;
  productoId: number;
  tipo: string;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface PresupuestoResponse {
  id: number;
  patenteVehiculo: string;
  fechaEmision: string;
  fechaVencimiento: string;
  estado: EstadoPresupuesto;
  observaciones: string;
  items: ItemPresupuestoResponse[];
  total: number;
}

export interface PresupuestoSummaryResponse {
  id: number;
  patenteVehiculo: string | null;
  fechaEmision: string;
  fechaVencimiento: string;
  estado: EstadoPresupuesto;
  total: number;
}

export interface PresupuestoRequest {
  vehiculoId: number | null;
  observaciones?: string;
}

export interface ItemPresupuestoRequest {
  productoId: number;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

export interface ModificarItemPresupuestoRequest {
  productoId: number | null;
  descripcion: string;
  cantidad: number;
  precioUnitario: number;
}

export interface AsociarVehiculoDatosVehiculoNuevo {
  patente: string;
  modeloId: number;
  anio: number;
  kilometrajeActual: number;
}

export interface AsociarVehiculoDatosClienteNuevo {
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
  email: string;
  direccion: string;
  situacionIva: SituacionIva;
}

export interface AsociarVehiculoAPresupuestoRequest {
  vehiculoId?: number;
  datosVehiculoNuevo?: AsociarVehiculoDatosVehiculoNuevo;
  clienteId?: number;
  datosClienteNuevo?: AsociarVehiculoDatosClienteNuevo;
}

export interface CambiarEstadoPresupuestoRequest {
  nuevoEstado: EstadoPresupuesto;
}

export const TRANSICIONES_VALIDAS: Record<EstadoPresupuesto, EstadoPresupuesto[]> = {
  PENDIENTE: ['APROBADO', 'RECHAZADO'],
  APROBADO: [],
  RECHAZADO: [],
  VENCIDO: [],
};