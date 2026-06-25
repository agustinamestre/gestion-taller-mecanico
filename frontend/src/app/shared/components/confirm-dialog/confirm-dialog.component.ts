import { Component, input, output } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [DialogModule, ButtonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss',
})
export class ConfirmDialogComponent {
  readonly visible = input(false);
  readonly titulo = input('Confirmar acción');
  readonly mensaje = input('¿Estás seguro de que querés continuar?');
  readonly labelConfirmar = input('Confirmar');
  readonly labelCancelar = input('Cancelar');
  readonly severityConfirmar = input<'danger' | 'warn' | 'success'>('danger');
  readonly cargando = input(false);

  readonly confirmado = output<void>();
  readonly cancelado = output<void>();
}