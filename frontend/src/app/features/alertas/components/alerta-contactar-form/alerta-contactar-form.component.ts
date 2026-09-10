import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { AlertaService } from '../../services/alerta.service';
import { MedioContacto } from '../../models/alerta.model';

@Component({
  selector: 'app-alerta-contactar-form',
  standalone: true,
  imports: [FormsModule, SelectModule, ButtonModule, TextareaModule],
  templateUrl: './alerta-contactar-form.component.html',
  styleUrl: './alerta-contactar-form.component.scss',
})
export class AlertaContactarFormComponent {
  readonly alertaService = inject(AlertaService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly medioContacto = signal<MedioContacto | null>(null);
  readonly observaciones = signal('');
  readonly enviado = signal(false);

  readonly opcionesMedio = [
    { label: 'Email', value: 'EMAIL' as MedioContacto },
    { label: 'WhatsApp', value: 'WHATSAPP' as MedioContacto },
  ];

  invalidMedio(): boolean {
    return this.enviado() && !this.medioContacto();
  }

  confirmar() {
    this.enviado.set(true);
    const medio = this.medioContacto();
    if (!medio) return;

    const alerta = this.alertaService.alertaSeleccionada();
    if (!alerta) return;

    this.alertaService.contactar(alerta.id, {
      medioContacto: medio,
      observaciones: this.observaciones().trim() || undefined,
    }).subscribe({
      next: () => this.guardado.emit(),
    });
  }
}
