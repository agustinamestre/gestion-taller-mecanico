import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { ClienteService } from '../../services/cliente.service';
import { ClienteRequest, SituacionIva } from '../../models/cliente.model';

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
  readonly soloDatos = input(false);
  readonly guardado = output<void>();
  readonly datosListos = output<ClienteRequest>();
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
    situacionIva: [null as SituacionIva | null, Validators.required],
    email: ['', [Validators.email]],
    direccion: [''],
  });

  ngOnInit() {
    const cliente = this.clienteService.clienteSeleccionado();
    if (this.edicion() && cliente) {
      this.form.patchValue({
        ...cliente,
        email: cliente.email ?? '',
        direccion: cliente.direccion ?? '',
      });
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

    const raw = this.form.getRawValue();
    const val: ClienteRequest = {
      dni: raw.dni!,
      nombre: raw.nombre!.trim(),
      apellido: raw.apellido!.trim(),
      telefono: raw.telefono!.trim(),
      situacionIva: raw.situacionIva!,
      email: raw.email?.trim() || null,
      direccion: raw.direccion?.trim() || null,
    };

    if (this.soloDatos()) {
      this.datosListos.emit(val);
      return;
    }

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