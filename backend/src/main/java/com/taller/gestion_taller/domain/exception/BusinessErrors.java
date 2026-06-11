package com.taller.gestion_taller.domain.exception;

import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
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

    public static BusinessError clienteNoEncontrado(Long id) {
        return new BusinessError("CLIENTE_NO_ENCONTRADO", "No se encontro un cliente con ID: " + id);
    }

    public static BusinessError clienteYaDadoDeBaja() {
        return new BusinessError("CLIENTE_YA_DESACTIVADO", "El cliente ya se encuentra inactivo.");
    }

    public static BusinessError operacionConClienteInactivo() {
        return new BusinessError("CLIENTE_INACTIVO", "No se puede realizar la operación ya que el cliente se encuentra inactivo.");
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
        return new BusinessError("MODELO_NO_ENCONTRADO", "No se encontro el modelo ingresado.");
    }

    public static BusinessError modeloYaDesactivado() {
        return new BusinessError("MODELO_YA_DESACTIVADO", "El modelo que ingreso se encuentra desactivado.");
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

    public static BusinessError vehiculoPatenteDuplicada() {
        return new BusinessError("VEHICULO_PATENTE_DUPLICADA", "Ya existe un vehiculo con la patente indicada");
    }

    public static BusinessError vehiculoNoEncontrado(Long id) {
        return new BusinessError("VEHICULO_NO_ENCONTRADO", "No se encontro el vehiculo con ID: " + id);
    }

    public static BusinessError vehiculoNoEncontrado(String patente) {
        return new BusinessError("VEHICULO_NO_ENCONTRADO", "No se encontro un vehiculo con patente: " + patente);
    }

    public static BusinessError vehiculoNoEncontrado() {
        return new BusinessError("VEHICULO_NO_ENCONTRADO", "No se encontro el vehiculo ingresado.");
    }

    public static BusinessError vehiculoYaDesactivado() {
        return new BusinessError("VEHICULO_YA_DESACTIVADO", "El vehiculo ya se encuentra desactivado.");
    }
    
    public static BusinessError vehiculoSinCliente() {
        return new BusinessError(
                "VEHICULO_SIN_CLIENTE",
                "Todo vehículo debe tener un cliente titular asociado."
        );
    }
    
    public static BusinessError vehiculoSinModelo() {
        return new BusinessError(
                "VEHICULO_SIN_MODELO",
                "Todo vehículo debe tener un modelo asociado."
        );
    }
    
    public static BusinessError vehiculoSinPatente() {
        return new BusinessError(
                "VEHICULO_SIN_PATENTE",
                "La patente del vehículo es obligatoria."
        );
    }
    
    public static BusinessError ordenSinVehiculo() {
        return new BusinessError(
                "ORDEN_SIN_VEHICULO",
                "Toda orden de trabajo debe tener un vehículo asociado."
        );
    }
    
    public static BusinessError ordenSinDescripcion() {
        return new BusinessError(
                "ORDEN_SIN_DESCRIPCION",
                "La descripción del problema es obligatoria."
        );
    }
    
    public static BusinessError ordenSinUsuarioCreacion() {
        return new BusinessError(
                "ORDEN_SIN_USUARIO_CREACION",
                "El usuario de creación es obligatorio."
        );
    }

    public static BusinessError presupuestoNoEncontrado(Long id) {
        return new BusinessError("PRESUPUESTO_NO_ENCONTRADO", "No se encontro el presupuesto con ID: " + id);
    }

    public static BusinessError presupuestoNoPendiente() {
        return new BusinessError("PRESUPUESTO_NO_PENDIENTE", "Solo se pueden agregar items a presupuestos en estado PENDIENTE.");
    }

    public static BusinessError presupuestoDebeEstarAprobado() {
        return new BusinessError(
                "PRESUPUESTO_NO_APROBADO",
                "El presupuesto debe estar en estado APROBADO para asociarlo a una orden de trabajo"
        );
    }

    public static BusinessError patenteInconsistenteConPresupuesto(String patenteRecibida, String patentePresupuesto) {
        return new BusinessError(
                "PATENTE_INCONSISTENTE_CON_PRESUPUESTO",
                MessageFormat.format(
                        "La patente recibida ''{0}'' no coincide con la del vehículo del presupuesto (''{1}'').",
                        patenteRecibida, patentePresupuesto)
        );
    }
    
    public static BusinessError presupuestoSinVehiculoAsociado() {
        return new BusinessError(
                "PRESUPUESTO_SIN_VEHICULO",
                "Para crear una orden de trabajo, el presupuesto debe estar asociado a un vehículo."
        );
    }
    
    public static BusinessError presupuestoYaTieneVehiculo() {
        return new BusinessError(
                "PRESUPUESTO_YA_TIENE_VEHICULO",
                "El presupuesto ya tiene un vehículo asociado. Para reemplazarlo, utilice la operación de reasociación."
        );
    }

    public static BusinessError itemNoEncontrado(Long id) {
        return new BusinessError("ITEM_NO_ENCONTRADO", "No se encontro el item con ID: " + id);
    }

    public static BusinessError transicionEstadoInvalida(EstadoPresupuesto actual, EstadoPresupuesto nuevo) {
        return new BusinessError(
                "TRANSICION_ESTADO_INVALIDA",
                "No se puede cambiar el estado de " + actual + " a " + nuevo
        );
    }

    public static BusinessError transicionEstadoInvalidaOrden(EstadoOrdenTrabajo actual, EstadoOrdenTrabajo nuevo) {
        return new BusinessError(
                "TRANSICION_ESTADO_INVALIDA_ORDEN",
                "No se puede cambiar el estado de " + actual + " a " + nuevo
        );
    }

    public static BusinessError ordenNoEncontrada(Long id) {
        return new BusinessError("ORDEN_NO_ENCONTRADA", "No se encontro la orden de trabajo con ID: " + id);
    }

    public static BusinessError itemOrdenNoEncontrado(Long id) {
        return new BusinessError("ITEM_ORDEN_NO_ENCONTRADO", "No se encontro el item con ID: " + id);
    }

}
