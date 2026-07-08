export interface MarcaResponse {
  id: number;
  nombre: string;
  activo: boolean;
}

export interface MarcaRequest {
  nombre: string;
}