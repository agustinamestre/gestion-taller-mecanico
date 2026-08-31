import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { NotificationService } from '../../../shared/services/notification.service';

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const notification = inject(NotificationService);
  const router = inject(Router);

  if (authService.usuario()?.rol === 'ADMIN') {
    return true;
  }

  notification.advertencia('No tenés permisos para acceder a esta sección.');
  return router.createUrlTree(['/dashboard']);
};
