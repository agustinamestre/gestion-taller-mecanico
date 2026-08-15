import { Component, computed, inject, OnInit, output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { ProductoService } from '../../services/producto.service';

@Component({
  selector: 'app-producto-actualizar-precio',
  standalone: true,
  imports: [ReactiveFormsModule, InputNumberModule, ButtonModule],
  templateUrl: './producto-actualizar-precio.component.html',
  styleUrl: './producto-actualizar-precio.component.scss',
})
export class ProductoActualizarPrecioComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly productoService = inject(ProductoService);

  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly producto = computed(() => this.productoService.productoSeleccionado());

  readonly form = this.fb.group({
    precioActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit() {
    const producto = this.producto();
    if (producto) {
      this.form.patchValue({ precioActual: producto.precioActual });
    }
  }

  campo(nombre: string): AbstractControl { return this.form.get(nombre)!; }
  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && c.touched;
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;
    const producto = this.producto()!;
    this.productoService.actualizarPrecio(producto.id, {
      nuevoPrecio: this.form.getRawValue().precioActual!,
    }).subscribe({ next: () => this.guardado.emit() });
  }
}