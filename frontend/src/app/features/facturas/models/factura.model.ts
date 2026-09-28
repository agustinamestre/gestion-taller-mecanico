import { ItemOrdenTrabajoResponse } from "../../ordenes/models/orden-trabajo.model";

export type FormaPago = 'EFECTIVO' | 'TRANSFERENCIA_BANCARIA';

export const FORMA_PAGO_LABELS: Record<FormaPago, string> = {
  EFECTIVO: 'Efectivo',
  TRANSFERENCIA_BANCARIA: 'Transferencia bancaria',
};

export type EstadoFactura = 'EMITIDA' | 'ANULADA';

export type TipoFactura = 'SENIA' | 'FINAL';

export type TipoComprobante = 'A' | 'B' | 'C';

export interface OrdenTrabajoFacturaResponse {
  id: number;
  patenteVehiculo: string;
  fechaIngreso: string;
  fechaEgreso: string | null;
  descripcionProblema: string;
  estado: string;
  itemsOrden: ItemOrdenTrabajoResponse[];
  total: number;
}

export interface FacturaResponse {
  id: number;
  numeroFactura: string;
  fechaEmision: string;
  formaPago: FormaPago;
  clienteDni: string;
  total: number;
  ordenTrabajo: OrdenTrabajoFacturaResponse;
  estado: EstadoFactura;
  motivoAnulacion: string | null;
  tipoComprobante: TipoComprobante;
  tipoFactura: TipoFactura;
  montoFacturado: number;
}

export interface GenerarFacturaRequest {
  ordenTrabajoId: number;
  formaPago: FormaPago;
  tipoFactura: TipoFactura;
  monto: number;
}

export interface AnularFacturaRequest {
  motivo: string;
}

export interface ConsultarFacturasFiltros {
  id?: number;
  numeroFactura?: string;
  clienteDni?: string;
  patenteVehiculo?: string;
  ordenTrabajoId?: number;
}