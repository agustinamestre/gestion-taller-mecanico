import { Component, computed, inject, signal } from '@angular/core';

const MONTO_VISIBLE_KEY = 'dashboard.montoVisible';
import { CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from './services/dashboard.service';
import { AuthService } from '../../core/auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  readonly dashboardService = inject(DashboardService);
  private readonly authService = inject(AuthService);

  readonly hoy = this.formatearFecha(new Date());
  readonly primerDiaDelMes = this.formatearFecha(new Date(new Date().getFullYear(), new Date().getMonth(), 1));

  readonly saludo = computed(() => {
    const hora = new Date().getHours();
    const momento = hora >= 6 && hora < 12
      ? 'Buenos días'
      : hora >= 12 && hora < 20
        ? 'Buenas tardes'
        : 'Buenas noches';

    const username = this.authService.usuario()?.username;
    return username ? `${momento}, ${this.capitalizar(username)}` : momento;
  });

  readonly montoVisible = signal(this.leerMontoVisible());

  constructor() {
    this.dashboardService.obtenerResumen().subscribe();
  }

  toggleMonto(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.montoVisible.update((v) => !v);
    try {
      localStorage.setItem(MONTO_VISIBLE_KEY, String(this.montoVisible()));
    } catch {}
  }

  private leerMontoVisible(): boolean {
    try {
      return localStorage.getItem(MONTO_VISIBLE_KEY) !== 'false';
    } catch {
      return true;
    }
  }

  private capitalizar(texto: string): string {
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }

  private formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
}