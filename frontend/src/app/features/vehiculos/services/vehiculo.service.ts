import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import {
  VehiculoResponse,
  VehiculoRequest,
  ModificarVehiculoRequest,
  ActualizarKilometrajeRequest,
} from '../models/vehiculo.model';

const API_BASE = `${environment.apiUrl}/vehiculos`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class VehiculoService {
  private readonly http = inject(HttpClient);
  private readonly notification = inject(NotificationService);

  readonly vehiculoActual = signal<VehiculoResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  buscarPorPatente(patente: string) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<VehiculoResponse>(`${API_BASE}/patente/${patente}`).pipe(
      tap((vehiculo) => {
        this.vehiculoActual.set(vehiculo);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: VehiculoRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<VehiculoResponse>(API_BASE, request).pipe(
      tap((nuevo) => {
        this.vehiculoActual.set(nuevo);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(id: number, request: ModificarVehiculoRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<VehiculoResponse>(`${API_BASE}/${id}`, request).pipe(
      tap((actualizado) => {
        this.vehiculoActual.set(actualizado);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  actualizarKilometraje(id: number, request: ActualizarKilometrajeRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.patch<VehiculoResponse>(`${API_BASE}/${id}/kilometraje`, request).pipe(
      tap((actualizado) => {
        this.vehiculoActual.set(actualizado);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  desactivar(id: number) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.delete<void>(`${API_BASE}/${id}`).pipe(
      tap(() => {
        this.vehiculoActual.update((v) => (v ? { ...v, activo: false } : v));
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.vehiculoActual.set(null);
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