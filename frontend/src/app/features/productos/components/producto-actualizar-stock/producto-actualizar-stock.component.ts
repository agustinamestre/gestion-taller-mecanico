import { Component, computed, inject, OnInit, output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ProductoService } from '../../services/producto.service';

@Component({
  selector: 'app-producto-actualizar-stock',
  standalone: true,
  imports: [ReactiveFormsModule, InputNumberModule, ButtonModule],
  templateUrl: './producto-actualizar-stock.component.html',
  styleUrl: './producto-actualizar-stock.component.scss',
})
export class ProductoActualizarStockComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly productoService = inject(ProductoService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly producto = computed(() => this.productoService.productoSeleccionado());

  readonly form = this.fb.group({
    stockActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit() {
    const producto = this.producto();
    if (producto) {
      this.form.patchValue({ stockActual: producto.stockActual });
    }
  }

  campo(nombre: string): AbstractControl { 
    return this.form.get(nombre)!;
   }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && c.touched;
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    const producto = this.producto()!;
    this.productoService.actualizarStock(producto.id, {
      nuevoStock: this.form.getRawValue().stockActual!,
    }).subscribe({ next: () => this.guardado.emit() });
  }
}