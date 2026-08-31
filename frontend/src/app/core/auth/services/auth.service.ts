import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';
import { NotificationService } from '../../../shared/services/notification.service';
import { LoginRequest, LoginResponse } from '../models/auth.model';
import { environment } from '../../../../environments/environment';

const API_BASE = `${environment.apiUrl}/auth`;
const SESSION_KEY = 'gema_session';

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

export interface SesionUsuario {
  username: string;
  rol: string;
}

interface SesionGuardada extends SesionUsuario {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly usuario = signal<SesionUsuario | null>(null);
  readonly token = signal<string | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly cargando = computed(() => this.estadoCarga() === 'cargando');
  readonly isAutenticado = computed(() => this.token() !== null);

  constructor() {
    this.restaurarSesion();
  }

  login(request: LoginRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<LoginResponse>(`${API_BASE}/login`, request).pipe(
      tap((respuesta) => {
        const sesion: SesionGuardada = {
          token: respuesta.token,
          username: respuesta.username,
          rol: respuesta.rol,
        };
        localStorage.setItem(SESSION_KEY, JSON.stringify(sesion));
        this.token.set(sesion.token);
        this.usuario.set({ username: sesion.username, rol: sesion.rol });
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  logout() {
    localStorage.removeItem(SESSION_KEY);
    this.token.set(null);
    this.usuario.set(null);
    this.router.navigate(['/login']);
  }

  private restaurarSesion() {
    const guardada = localStorage.getItem(SESSION_KEY);
    if (!guardada) {
      return;
    }

    try {
      const sesion: SesionGuardada = JSON.parse(guardada);
      this.token.set(sesion.token);
      this.usuario.set({ username: sesion.username, rol: sesion.rol });
    } catch {
      localStorage.removeItem(SESSION_KEY);
    }
  }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Usuario o contraseña incorrectos';

    this.error.set(mensaje);
    this.estadoCarga.set('error');
    this.notification.error(mensaje);
    return throwError(() => err);
  }
}
