import { Component, inject, output, signal, computed } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { ClienteService } from '../../services/cliente.service';
import { ClienteResponse } from '../../models/cliente.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-cliente-table',
  standalone: true,
  imports: [
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TooltipModule,
    ToggleSwitchModule,
    ConfirmDialogComponent,
  ],
  templateUrl: './cliente-table.component.html',
  styleUrl: './cliente-table.component.scss',
})
export class ClienteTableComponent {
  readonly clienteService = inject(ClienteService);
  private readonly location = inject(Location);

  readonly nuevo = output<void>();
  readonly editar = output<ClienteResponse>();
  readonly ver = output<ClienteResponse>();
  readonly dialogVisible = signal(false);
  readonly clienteADesactivar = signal<ClienteResponse | null>(null);
  readonly dialogReactivarVisible = signal(false);
  readonly clienteAReactivar = signal<ClienteResponse | null>(null);

  readonly filtro = signal('');
  readonly mostrarInactivos = signal(false);

  readonly clientesFiltrados = computed(() => {
    const texto = this.filtro().toLowerCase().trim();
    const base = this.mostrarInactivos()
      ? this.clienteService.clientes().filter((c) => !c.activo)
      : this.clienteService.clientesActivos();

    if (!texto) return base;

    return base.filter((c) => {
      const nombreCompleto = `${c.nombre} ${c.apellido}`.toLowerCase();
      const apellidoNombre = `${c.apellido} ${c.nombre}`.toLowerCase();
      const email = (c.email ?? '').toLowerCase();

      return (
        c.dni.includes(texto) ||
        c.nombre.toLowerCase().includes(texto) ||
        c.apellido.toLowerCase().includes(texto) ||
        email.includes(texto) ||
        nombreCompleto.includes(texto) ||
        apellidoNombre.includes(texto)
      );
    });
  });

  volver() {
    this.location.back();
  }

  abrirConfirmDesactivar(cliente: ClienteResponse) {
    this.clienteADesactivar.set(cliente);
    this.dialogVisible.set(true);
  }

  onConfirmado() {
    const cliente = this.clienteADesactivar();
    if (!cliente) return;
    this.clienteService.desactivar(cliente.dni).subscribe({
      next: () => this.cerrarDialog(),
    });
  }

  cerrarDialog() {
    this.dialogVisible.set(false);
    this.clienteADesactivar.set(null);
  }

  abrirConfirmReactivar(cliente: ClienteResponse) {
    this.clienteAReactivar.set(cliente);
    this.dialogReactivarVisible.set(true);
  }

  onConfirmadoReactivar() {
    const cliente = this.clienteAReactivar();
    if (!cliente) return;
    this.clienteService.reactivar(cliente.dni).subscribe({
      next: () => this.cerrarDialogReactivar(),
    });
  }

  cerrarDialogReactivar() {
    this.dialogReactivarVisible.set(false);
    this.clienteAReactivar.set(null);
  }
}