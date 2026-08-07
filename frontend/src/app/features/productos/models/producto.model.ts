export type TipoProducto = 'REPUESTO' | 'MANO_DE_OBRA';

export interface ProductoResponse {
  id: number;
  nombre: string;
  descripcion: string;
  tipo: TipoProducto;
  precioActual: number;
  stockActual: number;
}

export interface ProductoRequest {
  nombre: string;
  descripcion: string;
  tipo: string;
  precioActual: number;
  stockActual: number;
}

export interface ModificarProductoRequest {
  nombre: string;
  descripcion: string;
  tipo: string;
}

export interface ActualizarPrecioRequest {
  nuevoPrecio: number;
}

export interface ActualizarStockRequest {
  nuevoStock: number;
}