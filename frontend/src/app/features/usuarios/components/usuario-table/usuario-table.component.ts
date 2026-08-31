import { Component, inject, output, signal, computed } from '@angular/core';
import { Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-usuario-table',
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
  templateUrl: './usuario-table.component.html',
  styleUrl: './usuario-table.component.scss',
})
export class UsuarioTableComponent {
  readonly usuarioService = inject(UsuarioService);
  private readonly location = inject(Location);

  readonly nuevo = output<void>();
  readonly editar = output<UsuarioResponse>();

  readonly filtro = signal('');
  readonly mostrarInactivos = signal(false);
  readonly dialogVisible = signal(false);
  readonly usuarioADesactivar = signal<UsuarioResponse | null>(null);

  readonly usuariosFiltrados = computed(() => {
    const texto = this.filtro().toLowerCase().trim();
    const base = this.mostrarInactivos()
      ? this.usuarioService.usuarios().filter((u) => !u.activo)
      : this.usuarioService.usuariosActivos();

    if (!texto) return base;
    return base.filter(
      (u) =>
        u.username.toLowerCase().includes(texto) ||
        u.nombre.toLowerCase().includes(texto) ||
        u.apellido.toLowerCase().includes(texto) ||
        u.telefono.toLowerCase().includes(texto) ||
        (u.email ?? '').toLowerCase().includes(texto)
    );
  });

  volver() {
    this.location.back();
  }

  abrirConfirmDesactivar(usuario: UsuarioResponse) {
    this.usuarioADesactivar.set(usuario);
    this.dialogVisible.set(true);
  }

  onConfirmado() {
    const usuario = this.usuarioADesactivar();
    if (!usuario) return;
    this.usuarioService.desactivar(usuario.id).subscribe({
      next: () => this.cerrarDialog(),
    });
  }

  cerrarDialog() {
    this.dialogVisible.set(false);
    this.usuarioADesactivar.set(null);
  }
}
