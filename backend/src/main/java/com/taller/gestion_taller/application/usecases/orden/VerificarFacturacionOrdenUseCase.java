package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.model.Factura;
import com.taller.gestion_taller.domain.model.OrdenTrabajo;
import com.taller.gestion_taller.domain.repositories.FacturaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VerificarFacturacionOrdenUseCase implements VerificarFacturacionOrden {

    private final FacturaRepository facturaRepository;

    @Override
    public boolean estaTotalmenteFacturada(OrdenTrabajo orden) {
        List<Factura> facturasActivas = facturaRepository.findActivasByOrdenTrabajoId(orden.getId());
        return Factura.estaTotalmenteFacturada(orden.calcularTotal(), facturasActivas);
    }

    @Override
    public Map<Long, Boolean> estanTotalmenteFacturadas(List<OrdenTrabajo> ordenes) {
        List<Long> ordenIds = ordenes.stream().map(OrdenTrabajo::getId).toList();
        Map<Long, List<Factura>> facturasActivasPorOrdenId = facturaRepository.findActivasByOrdenTrabajoIds(ordenIds)
                .stream()
                .collect(Collectors.groupingBy(factura -> factura.getOrdenTrabajo().getId()));

        return ordenes.stream()
                .collect(Collectors.toMap(
                        OrdenTrabajo::getId,
                        orden -> Factura.estaTotalmenteFacturada(orden.calcularTotal(),
                                facturasActivasPorOrdenId.getOrDefault(orden.getId(), List.of()))));
    }
}
