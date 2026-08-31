import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { PerfilService } from '../../services/perfil.service';
import { NotificationService } from '../../../../shared/services/notification.service';

@Component({
  selector: 'app-cambiar-password',
  standalone: true,
  imports: [ReactiveFormsModule, PasswordModule, ButtonModule],
  templateUrl: './cambiar-password.component.html',
  styleUrl: './cambiar-password.component.scss',
})
export class CambiarPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly notification = inject(NotificationService);
  readonly perfilService = inject(PerfilService);

  readonly abierto = signal(false);
  readonly enviado = signal(false);

  readonly form = this.fb.group({
    passwordActual: ['', [Validators.required]],
    passwordNueva: ['', [Validators.required, Validators.minLength(6)]],
    confirmarPassword: ['', [Validators.required]],
  });

  campo(nombre: string): AbstractControl {
    return this.form.get(nombre)!;
  }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  abrir() {
    this.abierto.set(true);
  }

  cancelar() {
    this.form.reset();
    this.enviado.set(false);
    this.abierto.set(false);
  }

  guardar() {
    this.enviado.set(true);
    if (this.form.invalid) return;

    const val = this.form.getRawValue();
    if (val.passwordNueva !== val.confirmarPassword) {
      this.notification.advertencia('La nueva contraseña y su confirmación no coinciden.');
      return;
    }

    this.perfilService
      .cambiarPassword({ passwordActual: val.passwordActual!, passwordNueva: val.passwordNueva! })
      .subscribe({
        next: () => {
          this.notification.exito('Tu contraseña fue actualizada correctamente.');
          this.form.reset();
          this.enviado.set(false);
          this.abierto.set(false);
        },
      });
  }
}
