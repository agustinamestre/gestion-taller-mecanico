import { Component, inject, OnInit, signal } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { PerfilService } from './services/perfil.service';
import { NotificationService } from '../../shared/services/notification.service';
import { CambiarPasswordComponent } from './components/cambiar-password/cambiar-password.component';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, ButtonModule, CambiarPasswordComponent],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.scss',
})
export class PerfilComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly notification = inject(NotificationService);
  private readonly location = inject(Location);
  readonly perfilService = inject(PerfilService);

  readonly editando = signal(false);
  readonly enviado = signal(false);

  readonly form = this.fb.group({
    nombre: ['', [Validators.required]],
    apellido: ['', [Validators.required]],
  });

  ngOnInit() {
    this.perfilService.obtenerPerfil().subscribe();
  }

  volver() {
    this.location.back();
  }

  campo(nombre: string): AbstractControl {
    return this.form.get(nombre)!;
  }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  editar() {
    const perfil = this.perfilService.perfil();
    if (!perfil) return;
    this.form.patchValue({ nombre: perfil.nombre, apellido: perfil.apellido });
    this.enviado.set(false);
    this.editando.set(true);
  }

  cancelar() {
    this.editando.set(false);
  }

  guardar() {
    this.enviado.set(true);
    if (this.form.invalid) return;

    const val = this.form.getRawValue();
    this.perfilService
      .modificarPerfil({ nombre: val.nombre!, apellido: val.apellido! })
      .subscribe({
        next: () => {
          this.notification.exito('Tus datos fueron actualizados correctamente.');
          this.editando.set(false);
        },
      });
  }
}
