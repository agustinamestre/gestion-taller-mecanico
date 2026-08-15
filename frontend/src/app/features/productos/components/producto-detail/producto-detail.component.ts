import { Component, inject, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { ProductoService } from '../../services/producto.service';
import { ProductoResponse } from '../../models/producto.model';

@Component({
  selector: 'app-producto-detail',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './producto-detail.component.html',
  styleUrl: './producto-detail.component.scss',
})
export class ProductoDetailComponent {
  readonly productoService = inject(ProductoService);

  readonly volver = output<void>();
  readonly editar = output<ProductoResponse>();
}