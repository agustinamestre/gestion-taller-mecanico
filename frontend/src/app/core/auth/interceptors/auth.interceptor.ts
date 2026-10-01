import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { catchError, filter, switchMap, take, throwError } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from '../services/auth.service';

let refrescando = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.token();

  const authReq = agregarToken(req, token);

  return next(authReq).pipe(
    catchError((err: HttpErrorResponse) => {
      const esRutaAuth = req.url.includes('/auth/login')
        || req.url.includes('/auth/refresh')
        || req.url.includes('/auth/logout');

      if (err.status !== 401 || esRutaAuth) {
        return throwError(() => err);
      }

      return manejarRefresh(req, next, authService);
    })
  );
};

function agregarToken(req: HttpRequest<unknown>, token: string | null) {
  return token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;
}

function manejarRefresh(
  req: HttpRequest<unknown>,
  next: Parameters<HttpInterceptorFn>[1],
  authService: AuthService
) {
  if (!refrescando) {
    refrescando = true;
    refreshTokenSubject.next(null);

    return authService.refrescarToken().pipe(
      switchMap((respuesta) => {
        refrescando = false;
        refreshTokenSubject.next(respuesta.accessToken);
        return next(agregarToken(req, respuesta.accessToken));
      }),
      catchError((err) => {
        refrescando = false;
        authService.logout();
        return throwError(() => err);
      })
    );
  }

  // Ya hay un refresh en curso: esperamos a que termine y reintentamos con el token nuevo
  return refreshTokenSubject.pipe(
    filter((token) => token !== null),
    take(1),
    switchMap((token) => next(agregarToken(req, token)))
  );
}