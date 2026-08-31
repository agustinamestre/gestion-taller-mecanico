import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { UsuarioService } from '../../services/usuario.service';
import { ROLES } from '../../models/usuario.model';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, PasswordModule, ButtonModule, SelectModule],
  templateUrl: './usuario-form.component.html',
  styleUrl: './usuario-form.component.scss',
})
export class UsuarioFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly usuarioService = inject(UsuarioService);

  readonly edicion = input(false);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly roles = ROLES;
  readonly enviado = signal(false);
  readonly usernameActual = signal('');
  private usuarioId: number | null = null;

  readonly form = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    nombre: ['', [Validators.required]],
    apellido: ['', [Validators.required]],
    rol: [null as string | null, [Validators.required]],
  });

  ngOnInit() {
    const usuario = this.usuarioService.usuarioSeleccionado();
    if (this.edicion() && usuario) {
      this.usuarioId = usuario.id;
      this.usernameActual.set(usuario.username);
      this.form.patchValue({
        nombre: usuario.nombre,
        apellido: usuario.apellido,
        rol: usuario.rol,
      });
      this.form.get('username')?.clearValidators();
      this.form.get('username')?.updateValueAndValidity();
      this.form.get('password')?.clearValidators();
      this.form.get('password')?.updateValueAndValidity();
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

    const val = this.form.getRawValue();

    if (this.edicion() && this.usuarioId !== null) {
      this.usuarioService
        .modificar(this.usuarioId, {
          nombre: val.nombre!,
          apellido: val.apellido!,
          rol: val.rol as any,
        })
        .subscribe({
          next: () => this.guardado.emit(),
        });
    } else {
      this.usuarioService
        .registrar({
          username: val.username!,
          password: val.password!,
          nombre: val.nombre!,
          apellido: val.apellido!,
          rol: val.rol as any,
        })
        .subscribe({
          next: () => this.guardado.emit(),
        });
    }
  }
}
