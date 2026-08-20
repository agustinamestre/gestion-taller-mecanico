import { Component, inject, output, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { PresupuestoService } from '../../services/presupuesto.service';

@Component({
  selector: 'app-presupuesto-busqueda',
  standalone: true,
  imports: [CurrencyPipe, DatePipe, FormsModule, InputTextModule, ButtonModule, TableModule],
  templateUrl: './presupuesto-table.component.html',
  styleUrl: './presupuesto-table.component.scss',
})
export class PresupuestoTableComponent {
  readonly presupuestoService = inject(PresupuestoService);

  readonly nuevoPresupuesto = output<string | null>();
  readonly verDetalle = output<number>();

  readonly patente = signal('');

  constructor() {
    this.presupuestoService.listar().subscribe();
  }

  buscar() {
    const valor = this.patente().trim().toUpperCase();
    if (valor) {
      this.presupuestoService.buscarPorPatente(valor).subscribe();
    } else {
      this.presupuestoService.listar().subscribe();
    }
  }

  limpiarFiltro() {
    this.patente.set('');
    this.presupuestoService.listar().subscribe();
  }
}