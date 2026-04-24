package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findByDni(String dni);
    Optional<Cliente> findById(Long id);
    List<Cliente> findAll();
}
