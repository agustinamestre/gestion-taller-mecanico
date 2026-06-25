export interface VehiculoSummaryResponse {
  id: number;
  patente: string;
  marca: string;
  modelo: string;
  anio: number;
  kilometrajeActual: number;
  activo: boolean;
}

export interface ClienteResponse {
  id: number;
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
  email: string;
  direccion: string;
  activo: boolean;
  fechaCreacion: string;
  fechaModificacion: string;
  vehiculos?: VehiculoSummaryResponse[];
}

export interface ClienteRequest {
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
  email: string;
  direccion: string;
}

export interface ModificarClienteRequest {
  nombre: string;
  apellido: string;
  telefono: string;
  email: string;
  direccion: string;
}