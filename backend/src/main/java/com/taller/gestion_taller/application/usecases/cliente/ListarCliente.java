package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.domain.model.Cliente;

public interface ListarCliente {
    Cliente listarCliente(String nroDocumento);
}
