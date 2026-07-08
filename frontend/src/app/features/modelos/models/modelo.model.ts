import { MarcaResponse } from '../../marcas/models/marca.model';

export interface ModeloResponse {
  id: number;
  nombre: string;
  activo: boolean;
  marca: MarcaResponse;
}

export interface ModeloRequest {
  nombre: string;
  marcaId: number;
}