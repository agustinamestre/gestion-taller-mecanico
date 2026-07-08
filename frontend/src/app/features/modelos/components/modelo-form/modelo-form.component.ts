import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { ModeloService } from '../../services/modelo.service';
import { MarcaService } from '../../../marcas/services/marca.service';

@Component({
  selector: 'app-modelo-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule, SelectModule],
  templateUrl: './modelo-form.component.html',
  styleUrl: './modelo-form.component.scss',
})
export class ModeloFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly modeloService = inject(ModeloService);
  readonly marcaService = inject(MarcaService);

  readonly edicion = input(false);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly enviado = signal(false);
  private modeloId: number | null = null;

  readonly form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(1)]],
    marcaId: [null as number | null, [Validators.required]],
  });

  ngOnInit() {
    this.marcaService.listar().subscribe();

    const modelo = this.modeloService.modeloSeleccionado();
    if (this.edicion() && modelo) {
      this.modeloId = modelo.id;
      this.form.patchValue({
        nombre: modelo.nombre,
        marcaId: modelo.marca.id,
      });
    }
  }

  campo(nombre: string): AbstractControl {
    return this.form.get(nombre)!;
  }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  guardar() {
    this.enviado.set(true);
    if (this.form.invalid) return;

    const val = this.form.getRawValue() as { nombre: string; marcaId: number };

    if (this.edicion() && this.modeloId !== null) {
      this.modeloService.modificar(this.modeloId, val).subscribe({
        next: () => this.guardado.emit(),
      });
    } else {
      this.modeloService.registrar(val).subscribe({
        next: () => this.guardado.emit(),
      });
    }
  }
}