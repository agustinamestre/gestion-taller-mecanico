import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      {
        path: 'clientes',
        loadComponent: () =>
          import('./features/clientes/clientes.component').then(
            (c) => c.ClientesComponent
          ),
      },
      {
        path: 'marcas',
        loadComponent: () =>
          import('./features/marcas/marcas.component').then(
            (c) => c.MarcasComponent),
      },
      {
        path: 'modelos',
        loadComponent: () =>
        import('./features/modelos/modelos.component').then(
          (c) => c.ModelosComponent),
      },
      {
        path: 'vehiculos',
        loadComponent: () =>
          import('./features/vehiculos/vehiculos.component').then((c) => c.VehiculosComponent),
      },
      {
        path: 'productos',
        loadComponent: () => import('./features/productos/productos.component').then(c => c.ProductosComponent)
      },
      {
        path: 'presupuestos',
        loadComponent: () => import('./features/presupuestos/presupuestos.component').then(c => c.PresupuestosComponent)
      },
    ],
  },
  { path: '**', redirectTo: '' },
];