import { inject, Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly messageService = inject(MessageService);

  exito(detalle: string, titulo = 'Operación exitosa') {
    this.messageService.add({
      severity: 'success',
      summary: titulo,
      detail: detalle,
      life: 4000,
    });
  }

  error(detalle: string, titulo = 'Ocurrió un error') {
    this.messageService.add({
      severity: 'error',
      summary: titulo,
      detail: detalle,
      life: 5000,
    });
  }

  advertencia(detalle: string, titulo = 'Atención') {
    this.messageService.add({
      severity: 'warn',
      summary: titulo,
      detail: detalle,
      life: 4000,
    });
  }

  info(detalle: string, titulo = 'Información') {
    this.messageService.add({
      severity: 'info',
      summary: titulo,
      detail: detalle,
      life: 3000,
    });
  }
}