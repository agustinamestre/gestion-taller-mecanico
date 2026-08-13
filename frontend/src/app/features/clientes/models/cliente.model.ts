export type SituacionIva = 'RESPONSABLE_INSCRIPTO' | 'MONOTRIBUTISTA' | 'CONSUMIDOR_FINAL' | 'EXENTO';

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
  situacionIva: SituacionIva;
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
  situacionIva: SituacionIva;
}

export interface ModificarClienteRequest {
  dni: string;
  nombre: string;
  apellido: string;
  telefono: string;
  email: string;
  direccion: string;
  situacionIva: SituacionIva;
}