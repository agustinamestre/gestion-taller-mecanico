import { Component, inject, output, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { MarcaService } from '../../services/marca.service';
import { MarcaResponse } from '../../models/marca.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

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
    ConfirmDialogComponent,
  ],
  templateUrl: './marca-table.component.html',
  styleUrl: './marca-table.component.scss',
})
export class MarcaTableComponent {
  readonly marcaService = inject(MarcaService);

  readonly nuevo = output<void>();
  readonly editar = output<MarcaResponse>();

  readonly filtro = signal('');
  readonly mostrarInactivas = signal(false);

  readonly dialogVisible = signal(false);
  readonly marcaADesactivar = signal<MarcaResponse | null>(null);

  readonly marcasFiltradas = computed(() => {
    const texto = this.filtro().toLowerCase().trim();
    const base = this.mostrarInactivas()
      ? this.marcaService.marcas()
      : this.marcaService.marcasActivas();

    if (!texto) return base;
    return base.filter((m) => m.nombre.toLowerCase().includes(texto));
  });

  getSeverity(activo: boolean): 'success' | 'danger' {
    return activo ? 'success' : 'danger';
  }

  abrirConfirmDesactivar(marca: MarcaResponse) {
    this.marcaADesactivar.set(marca);
    this.dialogVisible.set(true);
  }

  onConfirmado() {
    const marca = this.marcaADesactivar();
    if (!marca) return;
    this.marcaService.desactivar(marca.id).subscribe({
      next: () => this.cerrarDialog(),
    });
  }

  cerrarDialog() {
    this.dialogVisible.set(false);
    this.marcaADesactivar.set(null);
  }
}