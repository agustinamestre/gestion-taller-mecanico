import { Component, effect, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule, AutoCompleteCompleteEvent, AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { ClienteResponse } from '../../../features/clientes/models/cliente.model';
import { ClienteService } from '../../../features/clientes/services/cliente.service';

interface ClienteAutoComplete extends ClienteResponse {
  nombreCompleto: string;
}

@Component({
  selector: 'app-cliente-selector',
  standalone: true,
  imports: [FormsModule, AutoCompleteModule],
  templateUrl: './cliente-selector.component.html',
  styleUrl: './cliente-selector.component.scss',
})
export class ClienteSelectorComponent implements OnInit {
  private readonly clienteService = inject(ClienteService);

  readonly clienteIdInicial = input<number | null>(null);
  readonly placeholder = input('Buscar por nombre, apellido o DNI...');
  readonly disabled = input(false);

  readonly clienteSeleccionado = output<ClienteResponse | null>();

  seleccion: ClienteAutoComplete | null = null;
  readonly sugerencias = signal<ClienteAutoComplete[]>([]);

  constructor() {
    effect(() => {
      const id = this.clienteIdInicial();
      const clientes = this.clienteService.clientes();
      if (id != null && clientes.length > 0 && id !== this.seleccion?.id) {
        const encontrado = clientes.find(c => c.id === id) ?? null;
        this.seleccion = encontrado ? this.toAutoComplete(encontrado) : null;
      }
    });
  }

  ngOnInit() {
    if (this.clienteService.clientes().length === 0) {
      this.clienteService.listar().subscribe();
    }
  }

  buscar(event: AutoCompleteCompleteEvent) {
    const texto = event.query.toLowerCase().trim();
    const base = this.clienteService.clientesActivos().map(c => this.toAutoComplete(c));

    this.sugerencias.set(
      texto
        ? base.filter(c =>
            c.nombre.toLowerCase().includes(texto) ||
            c.apellido.toLowerCase().includes(texto) ||
            c.dni.includes(texto)
          )
        : base
    );
  }

  onModelChange(value: ClienteAutoComplete | string | null) {
    if (value === null || typeof value === 'string') return;
    this.seleccion = value;
  }

onSeleccion(event: AutoCompleteSelectEvent) {
  const cliente = event.value as ClienteAutoComplete;
  this.seleccion = cliente;
  setTimeout(() => {
    this.clienteSeleccionado.emit(cliente);
  }, 0);
}

  onClear() {
    this.seleccion = null;
    this.clienteSeleccionado.emit(null);
  }

  private toAutoComplete(cliente: ClienteResponse): ClienteAutoComplete {
    return {
      ...cliente,
      nombreCompleto: `${cliente.nombre} ${cliente.apellido} — DNI ${cliente.dni}`,
    };
  }
}