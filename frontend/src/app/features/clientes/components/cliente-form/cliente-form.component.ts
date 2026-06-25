import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule],
  templateUrl: './cliente-form.component.html',
  styleUrl: './cliente-form.component.scss',
})
export class ClienteFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly clienteService = inject(ClienteService);

  readonly edicion = input(false);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly enviado = signal(false);

  readonly form = this.fb.group({
    dni: ['', [Validators.required, Validators.pattern(/^\d{7,8}$/)]],
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    telefono: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    direccion: ['', [Validators.required]],
  });

  ngOnInit() {
    const cliente = this.clienteService.clienteSeleccionado();
    if (this.edicion() && cliente) {
      this.form.patchValue(cliente);
      this.form.get('dni')?.disable();
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

    const val = this.form.getRawValue() as any;

    if (this.edicion()) {
      const { dni, ...resto } = val;
      this.clienteService.modificar(dni, resto).subscribe({
        next: () => this.guardado.emit(),
      });
    } else {
      this.clienteService.registrar(val).subscribe({
        next: () => this.guardado.emit(),
      });
    }
  }
}