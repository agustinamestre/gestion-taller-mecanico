package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.FormaPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReporteRepository {

    List<MontoPorFecha> facturacionPorDia(LocalDate desde, LocalDate hasta);

    List<MontoPorFormaPago> facturacionPorFormaPago(LocalDate desde, LocalDate hasta);

    List<CantidadPorFecha> ordenesIngresadasPorDia(LocalDate desde, LocalDate hasta);

    List<CantidadPorFecha> ordenesEntregadasPorDia(LocalDate desde, LocalDate hasta);

    List<FechasOrden> ordenesFinalizadas(LocalDate desde, LocalDate hasta);

    List<ServicioEnOrden> serviciosPorOrden(LocalDate desde, LocalDate hasta);

    record MontoPorFecha(LocalDate fecha, BigDecimal monto) {}

    record MontoPorFormaPago(FormaPago formaPago, BigDecimal monto, long cantidad) {}

    record CantidadPorFecha(LocalDate fecha, long cantidad) {}

    record FechasOrden(LocalDate fechaIngreso, LocalDate fechaFinalizacion) {}

    record ServicioEnOrden(Long productoId, String nombre, Long ordenId) {}
}
