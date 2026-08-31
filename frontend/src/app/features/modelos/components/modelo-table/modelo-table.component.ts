import { Component, inject, OnInit, output, signal, computed } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { SelectModule } from 'primeng/select';
import { ModeloService } from '../../services/modelo.service';
import { MarcaService } from '../../../marcas/services/marca.service';
import { ModeloResponse } from '../../models/modelo.model';

@Component({
  selector: 'app-modelo-table',
  standalone: true,
  imports: [
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TagModule,
    TooltipModule,
    ToggleSwitchModule,
    SelectModule,
  ],
  templateUrl: './modelo-table.component.html',
  styleUrl: './modelo-table.component.scss',
})
export class ModeloTableComponent implements OnInit {
  readonly modeloService = inject(ModeloService);
  readonly marcaService = inject(MarcaService);
  private readonly location = inject(Location);

  readonly nuevo = output<void>();
  readonly editar = output<ModeloResponse>();

  readonly filtroTexto = signal('');
  readonly marcaFiltro = signal<number | null>(null);
  readonly mostrarInactivos = signal(false);

  readonly modelosFiltrados = computed(() => {
    const marcaId = this.marcaFiltro();
    const texto = this.filtroTexto().trim().toLowerCase();

    return this.modeloService.modelos().filter((m) => {
      const coincideMarca = marcaId ? m.marca.id === marcaId : true;
      const coincideTexto = texto ? m.nombre.toLowerCase().includes(texto) : true;
      return coincideMarca && coincideTexto;
    });
  });

  volver() {
    this.location.back();
  }

  ngOnInit() {
    if (this.marcaService.marcas().length === 0) {
      this.marcaService.listar().subscribe();
    }
    if (this.modeloService.modelos().length === 0) {
      this.modeloService.listar().subscribe();
    }
  }

}