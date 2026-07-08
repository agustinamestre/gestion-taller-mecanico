import { Component, inject, OnInit, signal } from '@angular/core';
import { ModeloService } from './services/modelo.service';
import { NotificationService } from '../../shared/services/notification.service';
import { ModeloResponse } from './models/modelo.model';
import { ModeloTableComponent } from './components/modelo-table/modelo-table.component';
import { ModeloFormComponent } from './components/modelo-form/modelo-form.component';

export type VistaModelo = 'tabla' | 'form-nuevo' | 'form-editar';

@Component({
  selector: 'app-modelos',
  standalone: true,
  imports: [ModeloTableComponent, ModeloFormComponent],
  templateUrl: './modelos.component.html',
  styleUrl: './modelos.component.scss',
})
export class ModelosComponent implements OnInit {
  private readonly modeloService = inject(ModeloService);
  private readonly notification = inject(NotificationService);

  readonly vista = signal<VistaModelo>('tabla');
  private modoActual: VistaModelo = 'tabla';

  ngOnInit() {
    this.modeloService.listar().subscribe();
  }

  abrirNuevo() {
    this.modeloService.limpiarSeleccion();
    this.modoActual = 'form-nuevo';
    this.vista.set('form-nuevo');
  }

  editarModelo(modelo: ModeloResponse) {
    this.modeloService.modeloSeleccionado.set(modelo);
    this.modoActual = 'form-editar';
    this.vista.set('form-editar');
  }

  volver() {
    this.modeloService.limpiarSeleccion();
    this.vista.set('tabla');
  }

  onGuardado() {
    const mensaje =
      this.modoActual === 'form-nuevo'
        ? 'El modelo fue registrado correctamente.'
        : 'El modelo fue actualizado correctamente.';

    this.notification.exito(mensaje);
    this.modeloService.listar().subscribe();
    this.vista.set('tabla');
  }
}