import { Component, inject, OnInit, signal } from '@angular/core';
import { ClienteService } from './services/cliente.service';
import { ClienteTableComponent } from './components/cliente-table/cliente-table.component';
import { ClienteFormComponent } from './components/cliente-form/cliente-form.component';
import { ClienteDetailComponent } from './components/cliente-detail/cliente-detail.component';
import { ClienteResponse } from './models/cliente.model';
import { NotificationService } from '../../shared/services/notification.service';

export type VistaCliente = 'tabla' | 'form-nuevo' | 'form-editar' | 'detalle';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [ClienteTableComponent, ClienteFormComponent, ClienteDetailComponent],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.scss',
})
export class ClientesComponent implements OnInit {
  private readonly clienteService = inject(ClienteService);
  private readonly notification = inject(NotificationService);

  readonly vista = signal<VistaCliente>('tabla');
  private _modoActual: VistaCliente = 'tabla';

  ngOnInit() {
    this.clienteService.listar().subscribe();
  }

  abrirNuevo() {
    this.clienteService.limpiarSeleccion();
    this._modoActual = 'form-nuevo';
    this.vista.set('form-nuevo');
  }

  editarCliente(cliente: ClienteResponse) {
    this.clienteService.clienteSeleccionado.set(cliente);
    this._modoActual = 'form-editar';
    this.vista.set('form-editar');
  }

  onGuardado() {
    const mensaje = this._modoActual === 'form-nuevo'
      ? 'El cliente fue registrado correctamente.'
      : 'Los datos del cliente fueron actualizados.';

    this.notification.exito(mensaje);
    this.clienteService.listar().subscribe();
    this.vista.set('tabla');
  }

  onDesactivado() {
    this.notification.advertencia('El cliente fue desactivado del sistema.');
    this.vista.set('tabla');
  }

  verDetalle(cliente: ClienteResponse) {
    this.clienteService.clienteSeleccionado.set(cliente);
    this.vista.set('detalle');
  }

  volver() {
    this.clienteService.limpiarSeleccion();
    this.vista.set('tabla');
  }

  
}