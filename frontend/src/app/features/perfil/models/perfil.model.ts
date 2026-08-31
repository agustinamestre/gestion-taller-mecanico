export interface ModificarPerfilPropioRequest {
  nombre: string;
  apellido: string;
}

export interface CambiarPasswordRequest {
  passwordActual: string;
  passwordNueva: string;
}
