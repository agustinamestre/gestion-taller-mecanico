import { Component, inject, output, signal } from '@angular/core';
import { DatePipe, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { SelectModule } from 'primeng/select';
import { TooltipModule } from 'primeng/tooltip';
import { AlertaService } from '../../services/alerta.service';
import { Alerta } from '../../models/alerta.model';

type FiltroContactado = 'todas' | 'pendientes' | 'contactadas';

@Component({
  selector: 'app-alerta-table',
  standalone: true,
  imports: [
    DatePipe,
    FormsModule,
    InputTextModule,
    ButtonModule,
    TableModule,
    SelectModule,
    TooltipModule,
  ],
  templateUrl: './alerta-table.component.html',
  styleUrl: './alerta-table.component.scss',
})
export class AlertaTableComponent {
  readonly alertaService = inject(AlertaService);
  private readonly location = inject(Location);

  readonly contactar = output<Alerta>();
  readonly verDetalle = output<Alerta>();

  readonly patente = signal('');
  readonly filtroContactado = signal<FiltroContactado>('pendientes');

  readonly opcionesContactado = [
    { label: 'Todas', value: 'todas' as FiltroContactado },
    { label: 'Pendientes', value: 'pendientes' as FiltroContactado },
    { label: 'Contactadas', value: 'contactadas' as FiltroContactado },
  ];

  constructor() {
    this.buscar();
  }

  volver() {
    this.location.back();
  }

  buscar() {
    const patente = this.patente().trim().toUpperCase();
    const filtro = this.filtroContactado();

    this.alertaService.listar({
      patente: patente || undefined,
      contactado: filtro === 'todas' ? undefined : filtro === 'contactadas',
    }).subscribe();
  }

  limpiarFiltros() {
    this.patente.set('');
    this.filtroContactado.set('todas');
    this.buscar();
  }

  get tieneFiltrosActivos(): boolean {
    return !!this.patente().trim() || this.filtroContactado() !== 'todas';
  }
}
