import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { UsuarioResponse } from '../../usuarios/models/usuario.model';
import { NotificationService } from '../../../shared/services/notification.service';
import { environment } from '../../../../environments/environment';
import { CambiarPasswordRequest, ModificarPerfilPropioRequest } from '../models/perfil.model';

const API_BASE = `${environment.apiUrl}/usuarios`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class PerfilService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly perfil = signal<UsuarioResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  obtenerPerfil() {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<UsuarioResponse>(`${API_BASE}/me`).pipe(
      tap((perfil) => {
        this.perfil.set(perfil);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificarPerfil(request: ModificarPerfilPropioRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<UsuarioResponse>(`${API_BASE}/me`, request).pipe(
      tap((actualizado) => {
        this.perfil.set(actualizado);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  cambiarPassword(request: CambiarPasswordRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<void>(`${API_BASE}/me/password`, request).pipe(
      tap(() => this.estadoCarga.set('exito')),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
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
