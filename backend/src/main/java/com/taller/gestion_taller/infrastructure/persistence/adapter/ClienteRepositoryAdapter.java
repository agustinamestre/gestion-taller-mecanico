package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.ClienteEntity;
import com.taller.gestion_taller.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaClienteRepository;
    private final ClientePersistenceMapper persistenceMapper;

    @Override
    public Cliente save(Cliente cliente) {
        try {
            ClienteEntity entity = persistenceMapper.toEntity(cliente);
            ClienteEntity saved = jpaClienteRepository.save(entity);
            return persistenceMapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRunTimeException(BusinessErrors.clienteDniDuplicado(cliente.getDni()));
        }
    }
    
    @Override
    public Optional<Cliente> findByDni(String dni) {
        return jpaClienteRepository.findByDni(dni)
                .map(persistenceMapper::toDomain);
    }
}
