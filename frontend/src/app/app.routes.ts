import { Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { authGuard } from './core/auth/guards/auth.guard';
import { adminGuard } from './core/auth/guards/rol.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./core/auth/login/login.component').then((c) => c.LoginComponent),
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
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
      {
        path: 'alertas',
        loadComponent: () => import('./features/alertas/alertas.component').then(c => c.AlertasComponent)
      },
      {
        path: 'usuarios',
        canActivate: [adminGuard],
        loadComponent: () => import('./features/usuarios/usuarios.component').then(c => c.UsuariosComponent),
      },
      {
        path: 'perfil',
        loadComponent: () => import('./features/perfil/perfil.component').then(c => c.PerfilComponent),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];