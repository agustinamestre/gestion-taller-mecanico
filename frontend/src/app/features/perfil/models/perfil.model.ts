export interface ModificarPerfilPropioRequest {
  nombre: string;
  apellido: string;
  telefono: string;
  email: string | null;
  direccion: string;
}

export interface CambiarPasswordRequest {
  passwordActual: string;
  passwordNueva: string;
}
