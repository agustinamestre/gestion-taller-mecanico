import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { ClienteService } from '../../services/cliente.service';
import { SituacionIva } from '../../models/cliente.model';

interface SituacionIvaOpcion {
  label: string;
  value: SituacionIva;
}

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, SelectModule, ButtonModule],
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

  private dniOriginal: string | null = null;

  readonly situacionIvaOpciones: SituacionIvaOpcion[] = [
    { label: 'Responsable Inscripto', value: 'RESPONSABLE_INSCRIPTO' },
    { label: 'Monotributista', value: 'MONOTRIBUTISTA' },
    { label: 'Consumidor Final', value: 'CONSUMIDOR_FINAL' },
    { label: 'Exento', value: 'EXENTO' },
  ];

  readonly form = this.fb.group({
    dni: ['', [Validators.required, Validators.pattern(/^\d{7,8}$/)]],
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    telefono: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    direccion: ['', [Validators.required]],
    situacionIva: [null as SituacionIva | null, Validators.required],
  });

  ngOnInit() {
    const cliente = this.clienteService.clienteSeleccionado();
    if (this.edicion() && cliente) {
      this.form.patchValue(cliente);
      this.dniOriginal = cliente.dni;
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
      this.clienteService.modificar(this.dniOriginal!, val).subscribe({
        next: () => this.guardado.emit(),
      });
    } else {
      this.clienteService.registrar(val).subscribe({
        next: () => this.guardado.emit(),
      });
    }
  }
}