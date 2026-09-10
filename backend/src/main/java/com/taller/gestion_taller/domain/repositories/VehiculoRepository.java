package com.taller.gestion_taller.domain.repositories;

import com.taller.gestion_taller.domain.model.Vehiculo;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface VehiculoRepository {
    Vehiculo save(Vehiculo vehiculo);
    Optional<Vehiculo> findById(Long id);
    Optional<Vehiculo> findByPatente(String patente);
    List<Vehiculo> findByClienteId(Long clienteId);
    List<Vehiculo> findAll();
    List<Vehiculo> findByActivoTrue();
    List<Vehiculo> findByPatenteContainingAndActivoTrue(String patente);
    List<Vehiculo> findByActivoTrueAndFechaUltimoServiceBefore(LocalDate umbral);
    List<Vehiculo> findByIdIn(List<Long> ids);
}
