import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import { Alerta, ContactarAlertaRequest } from '../models/alerta.model';

const API_BASE = `${environment.apiUrl}/alertas`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class AlertaService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly listado = signal<Alerta[]>([]);
  readonly alertaSeleccionada = signal<Alerta | null>(null);
  readonly estado = signal<EstadoCarga>('idle');
  readonly mensajeError = signal<string | null>(null);
  readonly pendientesCount = signal<number>(0);

  readonly cargando = computed(() => this.estado() === 'cargando');

  listar(filtros?: { patente?: string; contactado?: boolean }) {
    this.estado.set('cargando');
    this.mensajeError.set(null);

    let params = new HttpParams();
    if (filtros?.patente) params = params.set('patente', filtros.patente);
    if (filtros?.contactado !== undefined && filtros.contactado !== null) {
      params = params.set('contactado', filtros.contactado);
    }

    return this.http.get<Alerta[]>(API_BASE, { params }).pipe(
      tap(lista => { this.listado.set(lista); this.estado.set('exito'); }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  contarPendientes() {
    return this.http.get<number>(`${API_BASE}/pendientes/count`).pipe(
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  cargarPendientesCount() {
    this.contarPendientes().subscribe({
      next: (count) => this.pendientesCount.set(count),
      error: () => {},
    });
  }

  contactar(id: number, request: ContactarAlertaRequest) {
    this.estado.set('cargando');
    this.mensajeError.set(null);

    return this.http.put<Alerta>(`${API_BASE}/${id}/contactar`, request).pipe(
      tap(actualizada => {
        this.estado.set('exito');
        this.toast.exito('Alerta marcada como contactada');
        this.listado.update(lista => lista.map(a => a.id === id ? actualizada : a));
        this.cargarPendientesCount();
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.alertaSeleccionada.set(null);
  }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Error inesperado del servidor';

    this.mensajeError.set(mensaje);
    this.estado.set('error');
    this.toast.error(mensaje);
    return throwError(() => err);
  }
}
