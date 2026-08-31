import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, PasswordModule, ButtonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  readonly authService = inject(AuthService);

  readonly enviado = signal(false);

  readonly form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  campo(nombre: string): AbstractControl {
    return this.form.get(nombre)!;
  }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  ingresar() {
    this.enviado.set(true);
    if (this.form.invalid) return;

    const { username, password } = this.form.getRawValue();
    this.authService.login({ username: username!, password: password! }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
    });
  }
}
