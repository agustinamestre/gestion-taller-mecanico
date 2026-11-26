import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import { ReporteResponse } from '../models/reporte.model';

const API_BASE = `${environment.apiUrl}/reportes`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly reporte = signal<ReporteResponse | null>(null);
  readonly estado = signal<EstadoCarga>('idle');

  readonly cargando = computed(() => this.estado() === 'cargando');

  obtener(desde: string, hasta: string) {
    this.estado.set('cargando');

    const params = new HttpParams().set('desde', desde).set('hasta', hasta);

    return this.http.get<ReporteResponse>(API_BASE, { params }).pipe(
      tap((reporte) => {
        this.reporte.set(reporte);
        this.estado.set('exito');
      }),
      catchError((err: HttpErrorResponse) => {
        this.estado.set('error');
        this.toast.error('Error al cargar el reporte');
        return throwError(() => err);
      })
    );
  }
}
