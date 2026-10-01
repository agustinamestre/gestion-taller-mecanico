import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';
import { NotificationService } from '../../../shared/services/notification.service';
import { LoginRequest, LoginResponse, RefrescarTokenResponse } from '../models/auth.model';
import { environment } from '../../../../environments/environment';

const API_BASE = `${environment.apiUrl}/auth`;
const SESSION_KEY = 'gma_session';

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

export interface SesionUsuario {
  username: string;
  rol: string;
}

interface SesionGuardada extends SesionUsuario {
  token: string;
  refreshToken: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly notification = inject(NotificationService);

  readonly usuario = signal<SesionUsuario | null>(null);
  readonly token = signal<string | null>(null);
  readonly refreshToken = signal<string | null>(null);
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
        this.guardarSesion({
          token: respuesta.token,
          refreshToken: respuesta.refreshToken,
          username: respuesta.username,
          rol: respuesta.rol,
        });
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  refrescarToken() {
    const refreshTokenActual = this.refreshToken();
    if (!refreshTokenActual) {
      return throwError(() => new Error('No hay refresh token disponible'));
    }

    return this.http
      .post<RefrescarTokenResponse>(`${API_BASE}/refresh`, { refreshToken: refreshTokenActual })
      .pipe(
        tap((respuesta) => {
          const sesionActual = this.usuario();
          this.guardarSesion({
            token: respuesta.accessToken,
            refreshToken: respuesta.refreshToken,
            username: sesionActual?.username ?? '',
            rol: sesionActual?.rol ?? '',
          });
        })
      );
  }

  logout() {
    const refreshTokenActual = this.refreshToken();
    const finalizarLocal = () => {
      localStorage.removeItem(SESSION_KEY);
      this.token.set(null);
      this.refreshToken.set(null);
      this.usuario.set(null);
      this.router.navigate(['/login']);
    };

    if (!refreshTokenActual) {
      finalizarLocal();
      return;
    }

    this.http.post<void>(`${API_BASE}/logout`, {}).subscribe({
      next: finalizarLocal,
      error: finalizarLocal,
    });
  }

  private guardarSesion(sesion: SesionGuardada) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(sesion));
    this.token.set(sesion.token);
    this.refreshToken.set(sesion.refreshToken);
    this.usuario.set({ username: sesion.username, rol: sesion.rol });
  }

  private restaurarSesion() {
    const guardada = localStorage.getItem(SESSION_KEY);
    if (!guardada) {
      return;
    }

    try {
      const sesion: SesionGuardada = JSON.parse(guardada);
      this.token.set(sesion.token);
      this.refreshToken.set(sesion.refreshToken);
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