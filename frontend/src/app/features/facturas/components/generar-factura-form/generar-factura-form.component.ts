import { Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { FacturaService } from '../../services/factura.service';
import { FormaPago, TipoFactura } from '../../models/factura.model';

@Component({
  selector: 'app-generar-factura-form',
  standalone: true,
  imports: [FormsModule, ButtonModule, SelectModule, InputTextModule, CurrencyPipe],
  templateUrl: './generar-factura-form.component.html',
  styleUrl: './generar-factura-form.component.scss',
})
export class GenerarFacturaFormComponent {
  readonly facturaService = inject(FacturaService);

  readonly ordenTrabajoId = input.required<number>();
  readonly saldoPendiente = input.required<number>();
  readonly puedeFacturarSenia = input(false);
  readonly puedeFacturarFinal = input(false);

  readonly generada = output<number>();
  readonly cancelar = output<void>();

  readonly formaPago = signal<FormaPago>('EFECTIVO');
  readonly tipoFactura = signal<TipoFactura>('FINAL');
  readonly monto = signal<number>(0);

  readonly opcionesFormaPago = [
    { label: 'Efectivo', value: 'EFECTIVO' as FormaPago },
    { label: 'Transferencia bancaria', value: 'TRANSFERENCIA_BANCARIA' as FormaPago },
  ];

  readonly opcionesTipoFactura = computed(() => {
    const opciones: { label: string; value: TipoFactura }[] = [];
    if (this.puedeFacturarSenia()) opciones.push({ label: 'Seña', value: 'SENIA' });
    if (this.puedeFacturarFinal()) opciones.push({ label: 'Final', value: 'FINAL' });
    return opciones;
  });

  readonly tituloFormulario = computed(() => (this.tipoFactura() === 'SENIA' ? 'Señar' : 'Generar factura'));

  readonly labelBotonConfirmar = computed(() => (this.tipoFactura() === 'SENIA' ? 'Generar seña' : 'Generar factura'));

  constructor() {
    effect(() => this.monto.set(this.saldoPendiente()));
    effect(() => {
      if (this.tipoFactura() === 'FINAL') this.monto.set(this.saldoPendiente());
    });
    effect(() => {
      const opciones = this.opcionesTipoFactura();
      if (!opciones.some((o) => o.value === this.tipoFactura()) && opciones.length > 0) {
        this.tipoFactura.set(opciones[0].value);
      }
    });
  }

  confirmar() {
    this.facturaService.generar({
      ordenTrabajoId: this.ordenTrabajoId(),
      formaPago: this.formaPago(),
      tipoFactura: this.tipoFactura(),
      monto: this.monto(),
    }).subscribe({
      next: (factura) => this.generada.emit(factura.id),
    });
  }
}
