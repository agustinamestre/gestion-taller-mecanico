import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import {
  UsuarioResponse,
  UsuarioRequest,
  ModificarUsuarioRequest,
} from '../models/usuario.model';
import { NotificationService } from '../../../shared/services/notification.service';
import { environment } from '../../../../environments/environment';

const API_BASE = `${environment.apiUrl}/usuarios`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly usuarios = signal<UsuarioResponse[]>([]);
  readonly usuarioSeleccionado = signal<UsuarioResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly usuariosActivos = computed(() =>
    this.usuarios().filter((u) => u.activo)
  );
  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  listar() {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<UsuarioResponse[]>(API_BASE).pipe(
      tap((usuarios) => {
        this.usuarios.set(usuarios);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: UsuarioRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<UsuarioResponse>(API_BASE, request).pipe(
      tap((nuevo) => {
        this.usuarios.update((lista) => [...lista, nuevo]);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: ModificarUsuarioRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<UsuarioResponse>(`${API_BASE}/${id}`, request).pipe(
      tap((actualizado) => {
        this.usuarios.update((lista) =>
          lista.map((u) => (u.id === id ? actualizado : u))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  desactivar(id: number) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.delete<void>(`${API_BASE}/${id}/desactivar`).pipe(
      tap(() => {
        this.usuarios.update((lista) =>
          lista.map((u) => (u.id === id ? { ...u, activo: false } : u))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.usuarioSeleccionado.set(null);
  }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Error inesperado del servidor';

    this.error.set(mensaje);
    this.estadoCarga.set('error');
    this.notification.error(mensaje);
    return throwError(() => err);
  }
}
