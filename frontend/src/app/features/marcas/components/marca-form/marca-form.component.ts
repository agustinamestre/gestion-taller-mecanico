import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MarcaService } from '../../services/marca.service';

@Component({
  selector: 'app-marca-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule],
  templateUrl: './marca-form.component.html',
  styleUrl: './marca-form.component.scss',
})
export class MarcaFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly marcaService = inject(MarcaService);

  readonly edicion = input(false);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly enviado = signal(false);
  private marcaId: number | null = null;

  readonly form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
  });

  ngOnInit() {
    const marca = this.marcaService.marcaSeleccionada();
    if (this.edicion() && marca) {
      this.marcaId = marca.id;
      this.form.patchValue({ nombre: marca.nombre });
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

    const val = this.form.getRawValue() as { nombre: string };

    if (this.edicion() && this.marcaId !== null) {
      this.marcaService.modificar(this.marcaId, val).subscribe({
        next: () => this.guardado.emit(),
      });
    } else {
      this.marcaService.registrar(val).subscribe({
        next: () => this.guardado.emit(),
      });
    }
  }
}