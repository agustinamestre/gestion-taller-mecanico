import { Component, inject, OnInit, output, signal, computed } from '@angular/core';
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
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

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
    ConfirmDialogComponent,
  ],
  templateUrl: './modelo-table.component.html',
  styleUrl: './modelo-table.component.scss',
})
export class ModeloTableComponent implements OnInit {
  readonly modeloService = inject(ModeloService);
  readonly marcaService = inject(MarcaService);

  readonly nuevo = output<void>();
  readonly editar = output<ModeloResponse>();

  readonly filtroTexto = signal('');
  readonly marcaFiltro = signal<number | null>(null);
  readonly mostrarInactivos = signal(false);

  readonly dialogVisible = signal(false);
  readonly modeloADesactivar = signal<ModeloResponse | null>(null);

  readonly modelosFiltrados = computed(() => {
    const texto = this.filtroTexto().toLowerCase().trim();
    const marcaId = this.marcaFiltro();

    const base = this.mostrarInactivos()
      ? this.modeloService.modelos()
      : this.modeloService.modelosActivos();

    return base.filter((m) => {
      const coincideMarca = marcaId ? m.marca.id === marcaId : true;
      const coincideTexto = texto ? m.nombre.toLowerCase().includes(texto) : true;
      return coincideMarca && coincideTexto;
    });
  });

  ngOnInit() {
    if (this.marcaService.marcas().length === 0) {
      this.marcaService.listar().subscribe();
    }
    if (this.modeloService.modelos().length === 0) {
      this.modeloService.listar().subscribe();
    }
  }

  getSeverity(activo: boolean): 'success' | 'danger' {
    return activo ? 'success' : 'danger';
  }

  abrirConfirmDesactivar(modelo: ModeloResponse) {
    this.modeloADesactivar.set(modelo);
    this.dialogVisible.set(true);
  }

  onConfirmado() {
    const modelo = this.modeloADesactivar();
    if (!modelo) return;
    this.modeloService.desactivar(modelo.id).subscribe({
      next: () => this.cerrarDialog(),
    });
  }

  cerrarDialog() {
    this.dialogVisible.set(false);
    this.modeloADesactivar.set(null);
  }
}