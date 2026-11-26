package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.model.EstadoFactura;
import com.taller.gestion_taller.domain.model.EstadoOrdenTrabajo;
import com.taller.gestion_taller.domain.model.FormaPago;
import com.taller.gestion_taller.domain.model.TipoProducto;
import com.taller.gestion_taller.domain.repositories.ReporteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReporteRepositoryAdapter implements ReporteRepository {

    private static final List<EstadoOrdenTrabajo> ESTADOS_TRABAJO_TERMINADO =
            List.of(EstadoOrdenTrabajo.FINALIZADO, EstadoOrdenTrabajo.ENTREGADO);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MontoPorFecha> facturacionPorDia(LocalDate desde, LocalDate hasta) {
        return em.createQuery("""
                        SELECT f.fechaEmision, SUM(f.montoFacturado)
                        FROM FacturaEntity f
                        WHERE f.estado = :estado AND f.fechaEmision BETWEEN :desde AND :hasta
                        GROUP BY f.fechaEmision
                        """, Object[].class)
                .setParameter("estado", EstadoFactura.EMITIDA)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new MontoPorFecha((LocalDate) r[0], (BigDecimal) r[1]))
                .toList();
    }

    @Override
    public List<MontoPorFormaPago> facturacionPorFormaPago(LocalDate desde, LocalDate hasta) {
        return em.createQuery("""
                        SELECT f.formaPago, SUM(f.montoFacturado), COUNT(f)
                        FROM FacturaEntity f
                        WHERE f.estado = :estado AND f.fechaEmision BETWEEN :desde AND :hasta
                        GROUP BY f.formaPago
                        """, Object[].class)
                .setParameter("estado", EstadoFactura.EMITIDA)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new MontoPorFormaPago((FormaPago) r[0], (BigDecimal) r[1], (Long) r[2]))
                .toList();
    }

    @Override
    public List<CantidadPorFecha> ordenesIngresadasPorDia(LocalDate desde, LocalDate hasta) {
        return em.createQuery("""
                        SELECT o.fechaIngreso, COUNT(o)
                        FROM OrdenTrabajoEntity o
                        WHERE o.estado <> :cancelado AND o.fechaIngreso BETWEEN :desde AND :hasta
                        GROUP BY o.fechaIngreso
                        """, Object[].class)
                .setParameter("cancelado", EstadoOrdenTrabajo.CANCELADO)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new CantidadPorFecha((LocalDate) r[0], (Long) r[1]))
                .toList();
    }

    @Override
    public List<CantidadPorFecha> ordenesEntregadasPorDia(LocalDate desde, LocalDate hasta) {
        return em.createQuery("""
                        SELECT o.fechaEgreso, COUNT(o)
                        FROM OrdenTrabajoEntity o
                        WHERE o.estado = :entregado AND o.fechaEgreso BETWEEN :desde AND :hasta
                        GROUP BY o.fechaEgreso
                        """, Object[].class)
                .setParameter("entregado", EstadoOrdenTrabajo.ENTREGADO)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new CantidadPorFecha((LocalDate) r[0], (Long) r[1]))
                .toList();
    }

    @Override
    public List<FechasOrden> ordenesFinalizadas(LocalDate desde, LocalDate hasta) {
        return em.createQuery("""
                        SELECT o.fechaIngreso, o.fechaFinalizacion
                        FROM OrdenTrabajoEntity o
                        WHERE o.estado IN :estados AND o.fechaFinalizacion BETWEEN :desde AND :hasta
                        """, Object[].class)
                .setParameter("estados", ESTADOS_TRABAJO_TERMINADO)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new FechasOrden((LocalDate) r[0], (LocalDate) r[1]))
                .toList();
    }

    // Los servicios pueden venir de los ítems de la orden o de los ítems de su presupuesto.
    @Override
    public List<ServicioEnOrden> serviciosPorOrden(LocalDate desde, LocalDate hasta) {
        List<ServicioEnOrden> servicios = new ArrayList<>();
        servicios.addAll(consultarServicios("""
                SELECT DISTINCT p.id, p.nombre, o.id
                FROM ItemOrdenTrabajoEntity i JOIN i.orden o JOIN i.producto p
                WHERE i.tipo = :tipo AND o.estado IN :estados
                AND o.fechaFinalizacion BETWEEN :desde AND :hasta
                """, desde, hasta));
        servicios.addAll(consultarServicios("""
                SELECT DISTINCT p.id, p.nombre, o.id
                FROM OrdenTrabajoEntity o JOIN o.presupuesto pr JOIN pr.items i JOIN i.producto p
                WHERE i.tipo = :tipo AND o.estado IN :estados
                AND o.fechaFinalizacion BETWEEN :desde AND :hasta
                """, desde, hasta));
        return servicios;
    }

    private List<ServicioEnOrden> consultarServicios(String jpql, LocalDate desde, LocalDate hasta) {
        return em.createQuery(jpql, Object[].class)
                .setParameter("tipo", TipoProducto.MANO_DE_OBRA)
                .setParameter("estados", ESTADOS_TRABAJO_TERMINADO)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList().stream()
                .map(r -> new ServicioEnOrden((Long) r[0], (String) r[1], (Long) r[2]))
                .toList();
    }
}
