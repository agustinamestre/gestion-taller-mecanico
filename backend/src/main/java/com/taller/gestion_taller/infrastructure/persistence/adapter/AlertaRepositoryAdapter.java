package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Alerta;
import com.taller.gestion_taller.domain.repositories.AlertaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.AlertaEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.AlertaPersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaAlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AlertaRepositoryAdapter implements AlertaRepository {

    private final JpaAlertaRepository jpaAlertaRepository;
    private final AlertaPersistenceMapper mapper;

    @Override
    public Alerta save(Alerta alerta) {
        if (alerta.getId() == null) {
            AlertaEntity entity = mapper.toEntity(alerta);
            return mapper.toDomain(jpaAlertaRepository.save(entity));
        }

        AlertaEntity entity = jpaAlertaRepository.findById(alerta.getId())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.alertaNoEncontrada(alerta.getId())));

        mapper.updateEntity(alerta, entity);
        return mapper.toDomain(jpaAlertaRepository.save(entity));
    }

    @Override
    public Optional<Alerta> findById(Long id) {
        return jpaAlertaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Alerta> findByFiltros(String patente, Boolean contactado) {
        return jpaAlertaRepository.findByFiltros(patente, contactado).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByContactadoFalse() {
        return jpaAlertaRepository.countByContactadoFalse();
    }

    @Override
    public boolean existsAlertaVigentePorVehiculo(Long vehiculoId, LocalDate fechaUltimoService) {
        return jpaAlertaRepository.existsByVehiculoIdAndFechaAlertaGreaterThanEqual(vehiculoId, fechaUltimoService);
    }
}
