import { Component, inject, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ClienteService } from '../../services/cliente.service';
import { ClienteResponse } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-detail',
  standalone: true,
  imports: [DatePipe, ButtonModule, TagModule],
  templateUrl: './cliente-detail.component.html',
  styleUrl: './cliente-detail.component.scss',
})
export class ClienteDetailComponent {
  readonly clienteService = inject(ClienteService);
  readonly editar = output<ClienteResponse>();
  readonly volver = output<void>();

  get cliente() {
    return this.clienteService.clienteSeleccionado();
  }
}