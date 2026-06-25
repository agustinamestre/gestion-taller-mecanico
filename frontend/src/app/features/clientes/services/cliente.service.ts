import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import {
  ClienteResponse,
  ClienteRequest,
  ModificarClienteRequest,
} from '../models/cliente.model';
import { NotificationService } from '../../../shared/services/notification.service';

const API_BASE = 'http://localhost:8090/gestion-taller/clientes';

export type EstadoCarga = 'idle' | 'cargando' | 'exito' | 'error';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private readonly http = inject(HttpClient);

  private readonly notification = inject(NotificationService);

  //State 
  readonly clientes = signal<ClienteResponse[]>([]);
  readonly clienteSeleccionado = signal<ClienteResponse | null>(null);
  readonly estadoCarga = signal<EstadoCarga>('idle');
  readonly error = signal<string | null>(null);

  //Computed 
  readonly clientesActivos = computed(() =>
    this.clientes().filter((c) => c.activo)
  );
  readonly totalClientes = computed(() => this.clientes().length);
  readonly cargando = computed(() => this.estadoCarga() === 'cargando');

  //Métodos 
  listar() {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<ClienteResponse[]>(API_BASE).pipe(
      tap((clientes) => {
        this.clientes.set(clientes);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  buscar(dni: string) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.get<ClienteResponse>(`${API_BASE}/${dni}`).pipe(
      tap((cliente) => {
        this.clienteSeleccionado.set(cliente);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  registrar(request: ClienteRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.post<ClienteResponse>(API_BASE, request).pipe(
      tap((nuevo) => {
        this.clientes.update((lista) => [...lista, nuevo]);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  modificar(dni: string, request: ModificarClienteRequest) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.put<ClienteResponse>(`${API_BASE}/${dni}`, request).pipe(
      tap((actualizado) => {
        this.clientes.update((lista) =>
          lista.map((c) => (c.dni === dni ? actualizado : c))
        );
        this.clienteSeleccionado.set(actualizado);
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  desactivar(dni: string) {
    this.estadoCarga.set('cargando');
    this.error.set(null);

    return this.http.delete<void>(`${API_BASE}/${dni}`).pipe(
      tap(() => {
        this.clientes.update((lista) =>
          lista.map((c) => (c.dni === dni ? { ...c, activo: false } : c))
        );
        this.estadoCarga.set('exito');
      }),
      catchError((err: HttpErrorResponse) => this.manejarError(err))
    );
  }

  limpiarSeleccion() {
    this.clienteSeleccionado.set(null);
  }

  private manejarError(err: HttpErrorResponse) {
    const mensaje = err.error?.mensaje ?? err.message ?? 'Error inesperado del servidor';
    this.error.set(mensaje);
    this.estadoCarga.set('error');
    this.notification.error(mensaje);  
    return throwError(() => err);
  }
}