import { Component, inject, output, signal, computed } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { MarcaService } from '../../services/marca.service';
import { MarcaResponse } from '../../models/marca.model';

@Component({
  selector: 'app-marca-table',
  standalone: true,
  imports: [
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TagModule,
    TooltipModule,
    ToggleSwitchModule,
  ],
  templateUrl: './marca-table.component.html',
  styleUrl: './marca-table.component.scss',
})
export class MarcaTableComponent {
  readonly marcaService = inject(MarcaService);
  private readonly location = inject(Location);

  readonly nuevo = output<void>();
  readonly editar = output<MarcaResponse>();

  readonly filtro = signal('');

  readonly marcasFiltradas = computed(() => {
    const texto = this.filtro().toLowerCase().trim();
    const base = this.marcaService.marcas();

    if (!texto) return base;
    return base.filter((m) => m.nombre.toLowerCase().includes(texto));
  });

  volver() {
    this.location.back();
  }

}