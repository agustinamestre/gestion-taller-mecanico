package com.taller.gestion_taller.application.usecases.orden;

import com.taller.gestion_taller.domain.model.OrdenTrabajo;

import java.util.List;
import java.util.Map;

public interface VerificarFacturacionOrden {

    boolean estaTotalmenteFacturada(OrdenTrabajo orden);

    Map<Long, Boolean> estanTotalmenteFacturadas(List<OrdenTrabajo> ordenes);
}
