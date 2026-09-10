import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';
import { BadgeModule } from 'primeng/badge';
import { MenuModule } from 'primeng/menu';
import { PopoverModule } from 'primeng/popover';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../core/auth/services/auth.service';
import { AlertaService } from '../features/alertas/services/alerta.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    InputTextModule,
    AvatarModule,
    BadgeModule,
    MenuModule,
    PopoverModule,
    ButtonModule,
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  readonly alertaService = inject(AlertaService);

  readonly currentYear = new Date().getFullYear();
  readonly sidebarAbierto = signal(false);
  readonly usuario = this.authService.usuario;

  constructor() {
    this.alertaService.cargarPendientesCount();
  }

  onAbrirPanelAlertas() {
    this.alertaService.listar({ contactado: false }).subscribe();
  }

  irAAlertas(popover: any) {
    popover.hide();
    this.router.navigate(['/alertas']);
  }

  readonly menuUsuario: MenuItem[] = [
    {
      label: 'Mi perfil',
      icon: 'pi pi-user',
      routerLink: '/perfil',
    },
    {
      label: 'Cerrar sesión',
      icon: 'pi pi-sign-out',
      command: () => this.authService.logout(),
    },
  ];

  get iniciales(): string {
    const username = this.usuario()?.username ?? '';
    return username.slice(0, 2).toUpperCase();
  }

  get esAdmin(): boolean {
    return this.usuario()?.rol === 'ADMIN';
  }

  toggleSidebar(): void {
    this.sidebarAbierto.update(abierto => !abierto);
  }

  cerrarSidebar(): void {
    this.sidebarAbierto.set(false);
  }
}
