export interface ClienteSummaryResponse {
  id: number;
  dni: string;
  nombre: string;
  apellido: string;
}

export interface VehiculoResponse {
  id: number;
  patente: string;
  marca: string;
  modelo: string;
  modeloId: number;
  anio: number;
  kilometrajeActual: number;
  cliente: ClienteSummaryResponse;
  activo: boolean;
  fechaUltimoService?: string;
}
export interface VehiculoRequest {
  patente: string;
  modeloId: number;
  anio: number;
  clienteId: number;
  kilometrajeActual: number;
  fechaUltimoService?: string;
}

export interface ModificarVehiculoRequest {
  modeloId: number;
  anio: number;
  clienteId: number;
  fechaUltimoService?: string;
}

export interface ActualizarKilometrajeRequest {
  kilometrajeActual: number;
}