import { Component, inject, output } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { UsuarioService } from '../../services/usuario.service';
import { UsuarioResponse } from '../../models/usuario.model';

@Component({
  selector: 'app-usuario-detail',
  standalone: true,
  imports: [ButtonModule],
  templateUrl: './usuario-detail.component.html',
  styleUrl: './usuario-detail.component.scss',
})
export class UsuarioDetailComponent {
  readonly usuarioService = inject(UsuarioService);

  readonly editar = output<UsuarioResponse>();
  readonly volver = output<void>();

  get usuario() {
    return this.usuarioService.usuarioSeleccionado();
  }

  formatearRol(rol: string): string {
    return rol === 'ADMIN' ? 'Admin' : 'Empleado';
  }
}
