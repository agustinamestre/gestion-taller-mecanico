export type AgrupacionReporte = 'DIA' | 'MES';

export type FormaPago = 'EFECTIVO' | 'TRANSFERENCIA_BANCARIA';

export interface PuntoFacturacion {
  fecha: string;
  monto: number;
}

export interface PuntoOrdenes {
  fecha: string;
  ingresadas: number;
  entregadas: number;
}

export interface ServicioTop {
  productoId: number;
  nombre: string;
  cantidadOrdenes: number;
}

export interface FacturacionFormaPago {
  formaPago: FormaPago;
  monto: number;
  cantidad: number;
}

export interface ReporteResponse {
  desde: string;
  hasta: string;
  agrupacion: AgrupacionReporte;
  totalFacturado: number;
  ordenesIngresadas: number;
  ordenesEntregadas: number;
  tiempoPromedioReparacionDias: number | null;
  facturacion: PuntoFacturacion[];
  ordenes: PuntoOrdenes[];
  topServicios: ServicioTop[];
  formasPago: FacturacionFormaPago[];
}
