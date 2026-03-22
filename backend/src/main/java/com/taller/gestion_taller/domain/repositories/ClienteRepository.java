package com.taller.gestion_taller.domain.repositories;

import java.util.Optional;

public interface ClienteRepository {
    Optional<Cliente> buscarPorDni(String dni);
    Cliente guardar(Cliente cliente);
}
