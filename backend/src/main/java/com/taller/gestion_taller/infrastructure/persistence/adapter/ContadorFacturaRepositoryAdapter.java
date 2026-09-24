package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.domain.repositories.ContadorFacturaRepository;
import com.taller.gestion_taller.infrastructure.persistence.entity.ContadorFacturaEntity;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaContadorFacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContadorFacturaRepositoryAdapter implements ContadorFacturaRepository {

    private static final Long ID = 1L;

    private final JpaContadorFacturaRepository jpaRepository;

    @Override
    public Long siguienteNumero() {
        ContadorFacturaEntity contador = jpaRepository.findConLockById(ID)
                .orElseGet(this::crearContadorInicial);

        contador.setUltimoNumero(contador.getUltimoNumero() + 1);
        jpaRepository.save(contador);

        return contador.getUltimoNumero();
    }

    private ContadorFacturaEntity crearContadorInicial() {
        try {
            return jpaRepository.save(ContadorFacturaEntity.builder().id(ID).ultimoNumero(0L).build());
        } catch (DataIntegrityViolationException ex) {
            return jpaRepository.findConLockById(ID)
                    .orElseThrow(() -> new IllegalStateException("No se pudo inicializar el contador de facturas"));
        }
    }
}
