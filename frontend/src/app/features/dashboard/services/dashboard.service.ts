import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import { DashboardResumenResponse } from '../models/dashboard.model';

const API_BASE = `${environment.apiUrl}/dashboard`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly resumen = signal<DashboardResumenResponse | null>(null);
  readonly estado = signal<EstadoCarga>('idle');

  readonly cargando = computed(() => this.estado() === 'cargando');

  obtenerResumen() {
    this.estado.set('cargando');

    return this.http.get<DashboardResumenResponse>(`${API_BASE}/resumen`).pipe(
      tap((resumen) => {
        this.resumen.set(resumen);
        this.estado.set('exito');
      }),
      catchError((err: HttpErrorResponse) => {
        this.estado.set('error');
        this.toast.error('Error al cargar el resumen del dashboard');
        return throwError(() => err);
      })
    );
  }
}
