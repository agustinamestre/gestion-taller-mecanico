package com.taller.gestion_taller.domain.exception;

import com.taller.gestion_taller.domain.model.TipoProducto;
import java.text.MessageFormat;

public final class BusinessErrors {

    private BusinessErrors() {}

    private static final String CAMPO_INVALIDO_MSG = "El campo {0} es invalido: {1}";

    public static BusinessError campoInvalido(String campo, String detalle) {
        return new BusinessError(campo.toUpperCase() + "_INVALIDO", MessageFormat.format(CAMPO_INVALIDO_MSG, campo, detalle));
    }

    public static BusinessError clienteDniDuplicado(String dni) {
        return new BusinessError("CLIENTE_DNI_DUPLICADO", "Ya existe un cliente con DNI: " + dni, "Intento de registro con DNI duplicado: " + dni);
    }

    public static BusinessError clienteNoEncontrado(String dni) {
        return new BusinessError("CLIENTE_NO_ENCONTRADO", "No se encontro un cliente con DNI: " + dni, "Busqueda fallida para DNI: " + dni);
    }

    public static BusinessError clienteYaDadoDeBaja() {
        return new BusinessError("CLIENTE_YA_DESACTIVADO", "El cliente ya se encuentra inactivo.");
    }

    public static BusinessError marcaDuplicada(String nombre) {
        return new BusinessError("MARCA_DUPLICADA", "Ya existe una marca con el nombre: " + nombre);
    }

    public static BusinessError marcaYaDesactivada() {
        return new BusinessError("MARCA_YA_DESACTIVADA", "La marca ya se encuentra desactivada.");
    }

    public static BusinessError marcaNoEncontrada() {
        return new BusinessError("MARCA_NO_ENCONTRADA", "No se encontro la marca ingresada.");
    }

    public static BusinessError modeloDuplicado(String nombre, String marca) {
        return new BusinessError("MODELO_DUPLICADO", MessageFormat.format("Ya existe el modelo {0} para la marca {1}", nombre, marca));
    }

    public static BusinessError modeloNoEncontrado() {
        return new BusinessError("MODELO_NO_ENCONTRADA", "No se encontro el modelo ingresado.");
    }

    public static BusinessError modeloYaDesactivado() {
        return new BusinessError("MODELO_YA_DESACTIVADO", "El modelo ya se encuentra desactivado.");
    }

    public static BusinessError productoDuplicado(String nombre, TipoProducto tipo) {
        return new BusinessError("PRODUCTO_DUPLICADO", MessageFormat.format("Ya existe un producto con el nombre {0} y tipo {1}", nombre, tipo));
    }

    public static BusinessError productoNoEncontrado(Long id) {
        return new BusinessError("PRODUCTO_NO_ENCONTRADO", "No se encontro el producto con ID: " + id);
    }

    public static BusinessError productosNoEncontradosPorTipo(String tipo) {
        return new BusinessError("PRODUCTOS_NO_ENCONTRADOS", "No se encontraron productos del tipo: " + tipo);
    }

    public static BusinessError productoYaDesactivado() {
        return new BusinessError("PRODUCTO_YA_DESACTIVADO", "El producto ya se encuentra desactivado.");
    }

}
