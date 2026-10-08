import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { AlertaService } from '../../services/alerta.service';

@Component({
  selector: 'app-alerta-contactar-form',
  standalone: true,
  imports: [FormsModule, ButtonModule, TextareaModule],
  templateUrl: './alerta-contactar-form.component.html',
  styleUrl: './alerta-contactar-form.component.scss',
})
export class AlertaContactarFormComponent {
  readonly alertaService = inject(AlertaService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly observaciones = signal('');

  confirmar() {
    const alerta = this.alertaService.alertaSeleccionada();
    if (!alerta) return;

    this.abrirWhatsapp(alerta);

    this.alertaService.contactar(alerta.id, {
      observaciones: this.observaciones().trim() || undefined,
    }).subscribe({
      next: () => this.guardado.emit(),
    });
  }

  private abrirWhatsapp(alerta: { telefonoCliente: string | null; nombreCliente: string; patenteVehiculo: string }) {
    const telefono = this.normalizarTelefonoArgentino(alerta.telefonoCliente);
    if (!telefono) return;

    const mensaje =
      `Hola ${alerta.nombreCliente}!\n\n` +
      `Te recordamos que el service de tu vehículo *${alerta.patenteVehiculo}* ya está en fecha o kilometraje de service.\n\n` +
      `Contactanos para coordinar un turno y mantener tu vehículo al día.\n\n` +
      `— *G.M.A. Gestión y Mantenimiento Automotriz*`;

    window.open(`https://wa.me/${telefono}?text=${encodeURIComponent(mensaje)}`, '_blank');
  }

  private normalizarTelefonoArgentino(telefono: string | null): string | null {
    let digitos = telefono?.replace(/\D/g, '').replace(/^00/, '');
    if (!digitos) return null;
    if (digitos.startsWith('549')) return digitos;
    if (digitos.startsWith('54')) digitos = digitos.slice(2);
    return `549${digitos.replace(/^0/, '')}`;
  }
}
