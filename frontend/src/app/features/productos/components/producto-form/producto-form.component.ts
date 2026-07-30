import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { ProductoService } from '../../services/producto.service';

interface TipoOpcion {
  label: string;
  value: string;
}

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputTextModule, InputNumberModule, SelectModule, TextareaModule, ButtonModule],
  templateUrl: './producto-form.component.html',
  styleUrl: './producto-form.component.scss',
})
export class ProductoFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly productoService = inject(ProductoService);

  readonly edicion = input(false);
  readonly guardado = output<void>();
  readonly cancelar = output<void>();

  readonly enviado = signal(false);

  readonly tiposOpciones: TipoOpcion[] = [
    { label: 'Repuesto', value: 'REPUESTO' },
    { label: 'Mano de obra', value: 'MANO_DE_OBRA' },
  ];

  readonly form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    descripcion: ['', Validators.required],
    tipo: [null as string | null, Validators.required],
    precioActual: [null as number | null, [Validators.required, Validators.min(0)]],
    stockActual: [null as number | null, [Validators.required, Validators.min(0)]],
  });

  ngOnInit() {
    const producto = this.productoService.productoSeleccionado();
    if (this.edicion() && producto) {
      this.form.patchValue({
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        tipo: producto.tipo,
        precioActual: producto.precioActual,
        stockActual: producto.stockActual,
      });
      // En edición, precio y stock se manejan con dialogs dedicados desde el detalle
      this.form.get('precioActual')?.disable();
      this.form.get('stockActual')?.disable();
    }
  }

  campo(nombre: string): AbstractControl { return this.form.get(nombre)!; }

  invalid(nombre: string): boolean {
    const c = this.campo(nombre);
    return c.invalid && (c.touched || this.enviado());
  }

  guardar() {
    this.enviado.set(true);
    if (this.form.invalid) return;
    const val = this.form.getRawValue();

    if (this.edicion()) {
      const producto = this.productoService.productoSeleccionado()!;
      this.productoService.modificar(producto.id, {
        nombre: val.nombre!,
        descripcion: val.descripcion!,
        tipo: val.tipo!,
      }).subscribe({ next: () => this.guardado.emit() });
    } else {
      this.productoService.registrar({
        nombre: val.nombre!,
        descripcion: val.descripcion!,
        tipo: val.tipo!,
        precioActual: val.precioActual!,
        stockActual: val.stockActual!,
      }).subscribe({ next: () => this.guardado.emit() });
    }
  }
}