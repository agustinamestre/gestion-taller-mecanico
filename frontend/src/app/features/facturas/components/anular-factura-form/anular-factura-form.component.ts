import { Component, inject, input, output } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TextareaModule } from 'primeng/textarea';
import { signal } from '@angular/core';
import { FacturaService } from '../../services/factura.service';

@Component({
  selector: 'app-anular-factura-form',
  standalone: true,
  imports: [CurrencyPipe, FormsModule, ButtonModule, TextareaModule],
  templateUrl: './anular-factura-form.component.html',
  styleUrl: './anular-factura-form.component.scss',
})
export class AnularFacturaFormComponent {
  readonly facturaService = inject(FacturaService);

  readonly facturaId = input.required<number>();
  readonly numeroFactura = input.required<string>();
  readonly total = input.required<number>();

  readonly anulada = output<void>();
  readonly cancelar = output<void>();

  readonly motivo = signal('');

  confirmar() {
    const motivo = this.motivo().trim();
    if (!motivo) return;

    this.facturaService.anular(this.facturaId(), { motivo }).subscribe({
      next: () => this.anulada.emit(),
    });
  }
}