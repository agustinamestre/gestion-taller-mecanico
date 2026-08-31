import { Component, inject, OnInit, signal } from '@angular/core';
import { UsuarioService } from './services/usuario.service';
import { NotificationService } from '../../shared/services/notification.service';
import { UsuarioResponse } from './models/usuario.model';
import { UsuarioTableComponent } from './components/usuario-table/usuario-table.component';
import { UsuarioFormComponent } from './components/usuario-form/usuario-form.component';

export type VistaUsuario = 'tabla' | 'form-nuevo' | 'form-editar';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [UsuarioTableComponent, UsuarioFormComponent],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss',
})
export class UsuariosComponent implements OnInit {
  private readonly usuarioService = inject(UsuarioService);
  private readonly notification = inject(NotificationService);

  readonly vista = signal<VistaUsuario>('tabla');
  private modoActual: VistaUsuario = 'tabla';

  ngOnInit() {
    this.usuarioService.listar().subscribe();
  }

  abrirNuevo() {
    this.usuarioService.limpiarSeleccion();
    this.modoActual = 'form-nuevo';
    this.vista.set('form-nuevo');
  }

  editarUsuario(usuario: UsuarioResponse) {
    this.usuarioService.usuarioSeleccionado.set(usuario);
    this.modoActual = 'form-editar';
    this.vista.set('form-editar');
  }

  volver() {
    this.usuarioService.limpiarSeleccion();
    this.vista.set('tabla');
  }

  onGuardado() {
    const mensaje =
      this.modoActual === 'form-nuevo'
        ? 'El usuario fue registrado correctamente.'
        : 'El usuario fue actualizado correctamente.';

    this.notification.exito(mensaje);
    this.usuarioService.listar().subscribe();
    this.vista.set('tabla');
  }
}
