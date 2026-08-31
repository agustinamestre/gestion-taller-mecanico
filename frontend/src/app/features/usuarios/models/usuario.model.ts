export type Rol = 'ADMIN' | 'EMPLEADO';

export const ROLES: Rol[] = ['ADMIN', 'EMPLEADO'];

export interface UsuarioResponse {
  id: number;
  username: string;
  nombre: string;
  apellido: string;
  rol: Rol;
  activo: boolean;
}

export interface UsuarioRequest {
  username: string;
  password: string;
  nombre: string;
  apellido: string;
  rol: Rol;
}

export interface ModificarUsuarioRequest {
  nombre: string;
  apellido: string;
  rol: Rol;
}
