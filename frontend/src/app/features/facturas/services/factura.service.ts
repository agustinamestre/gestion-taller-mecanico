import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { NotificationService } from '../../../shared/services/notification.service';
import {
  FacturaResponse,
  GenerarFacturaRequest,
  AnularFacturaRequest,
  ConsultarFacturasFiltros,
} from '../models/factura.model';

const API_BASE = `${environment.apiUrl}/facturas`;

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class FacturaService {
  private readonly http = inject(HttpClient);
  private readonly toast = inject(NotificationService);

  readonly listado = signal<FacturaResponse[]>([]);
  readonly seleccionada = signal<FacturaResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  obtener(id: number) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    const params = new HttpParams().set('id', id);
    return this.http.get<FacturaResponse[]>(API_BASE, { params }).pipe(
      tap((facturas) => {
        this.seleccionada.set(facturas[0] ?? null);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  listar(filtros?: ConsultarFacturasFiltros) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    let params = new HttpParams();
    if (filtros?.id != null) params = params.set('id', filtros.id);
    if (filtros?.numeroFactura) params = params.set('numeroFactura', filtros.numeroFactura);
    if (filtros?.clienteDni) params = params.set('clienteDni', filtros.clienteDni);
    if (filtros?.patenteVehiculo) params = params.set('patenteVehiculo', filtros.patenteVehiculo);
    if (filtros?.ordenTrabajoId != null) params = params.set('ordenTrabajoId', filtros.ordenTrabajoId);
    if (filtros?.desde) params = params.set('desde', filtros.desde);
    if (filtros?.hasta) params = params.set('hasta', filtros.hasta);

    return this.http.get<FacturaResponse[]>(API_BASE, { params }).pipe(
      tap((facturas) => {
        this.listado.set(facturas);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  obtenerActivasPorOrden(ordenTrabajoId: number) {
    const params = new HttpParams().set('ordenTrabajoId', ordenTrabajoId);
    return this.http.get<FacturaResponse[]>(API_BASE, { params }).pipe(
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  generar(request: GenerarFacturaRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<FacturaResponse>(API_BASE, request).pipe(
      tap((factura) => {
        this.listado.update((actual) => [factura, ...actual]);
        this.seleccionada.set(factura);
        this.estadoCarga.set('exito');
        this.toast.exito('Factura generada correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  anular(id: number, request: AnularFacturaRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.patch<FacturaResponse>(`${API_BASE}/${id}/anular`, request).pipe(
      tap((factura) => {
        this.listado.update((actual) =>
          actual.map((f) => (f.id === factura.id ? factura : f))
        );
        this.seleccionada.set(factura);
        this.estadoCarga.set('exito');
        this.toast.exito('Factura anulada correctamente');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.seleccionada.set(null);
  }

  private manejarError(err: HttpErrorResponse) {
    const errores = err.error?.error;
    const mensaje = Array.isArray(errores) && errores.length > 0
      ? errores.map((e: any) => e.errorMessage).join(', ')
      : 'Error inesperado del servidor';

    this.error.set(mensaje);
    this.estadoCarga.set('error');
    this.toast.error(mensaje);
    return throwError(() => err);
  }
}
