import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { VehiculoService } from '../../services/vehiculo.service';

@Component({
  selector: 'app-vehiculo-search',
  standalone: true,
  imports: [FormsModule, ButtonModule, InputTextModule],
  templateUrl: './vehiculo-search.component.html',
  styleUrl: './vehiculo-search.component.scss',
})
export class VehiculoSearchComponent {
  readonly vehiculoService = inject(VehiculoService);

  readonly vehiculoEncontrado = output<void>();
  readonly nuevo = output<void>();

  readonly patente = signal('');

  buscar() {
    const patente = this.patente().trim().toUpperCase();
    if (!patente) return;

    this.vehiculoService.buscarPorPatente(patente).subscribe({
      next: () => this.vehiculoEncontrado.emit(),
    });
  }

  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter') this.buscar();
  }
}