import { Component, inject, OnInit, signal } from '@angular/core';
import { MarcaService } from './services/marca.service';
import { NotificationService } from '../../shared/services/notification.service';
import { MarcaResponse } from './models/marca.model';
import { MarcaTableComponent } from './components/marca-table/marca-table.component';
import { MarcaFormComponent } from './components/marca-form/marca-form.component';

export type VistaMarca = 'tabla' | 'form-nuevo' | 'form-editar';

@Component({
  selector: 'app-marcas',
  standalone: true,
  imports: [MarcaTableComponent, MarcaFormComponent],
  templateUrl: './marcas.component.html',
  styleUrl: './marcas.component.scss',
})
export class MarcasComponent implements OnInit {
  private readonly marcaService = inject(MarcaService);
  private readonly notification = inject(NotificationService);

  readonly vista = signal<VistaMarca>('tabla');
  private modoActual: VistaMarca = 'tabla';

  ngOnInit() {
    this.marcaService.listar().subscribe();
  }

  abrirNuevo() {
    this.marcaService.limpiarSeleccion();
    this.modoActual = 'form-nuevo';
    this.vista.set('form-nuevo');
  }

  editarMarca(marca: MarcaResponse) {
    this.marcaService.marcaSeleccionada.set(marca);
    this.modoActual = 'form-editar';
    this.vista.set('form-editar');
  }

  volver() {
    this.marcaService.limpiarSeleccion();
    this.vista.set('tabla');
  }

  onGuardado() {
    const mensaje =
      this.modoActual === 'form-nuevo'
        ? 'La marca fue registrada correctamente.'
        : 'La marca fue actualizada correctamente.';

    this.notification.exito(mensaje);
    this.marcaService.listar().subscribe();
    this.vista.set('tabla');
  }
}