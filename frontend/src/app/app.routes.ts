import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  {
    // Ruta padre principal
    path: '',
    component: LayoutComponent,
    children: [
      // Redirección por defecto
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      
      // --- RUTAS CON LAZY LOADING ---
      /*
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(c => c.DashboardComponent)
      },
      {
        path: 'clientes',
        loadComponent: () => import('./features/clientes/clientes.component').then(c => c.ClientesComponent)
      },
      {
        path: 'vehiculos',
        loadComponent: () => import('./features/vehiculos/vehiculos.component').then(c => c.VehiculosComponent)
      },
      {
        path: 'ordenes',
        loadComponent: () => import('./features/ordenes-trabajo/ordenes-trabajo.component').then(c => c.OrdenesTrabajoComponent)
      }
      */
    ]
  },
  { path: '**', redirectTo: '' }
];
