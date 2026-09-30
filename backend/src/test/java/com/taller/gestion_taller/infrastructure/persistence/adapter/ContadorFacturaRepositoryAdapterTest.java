package com.taller.gestion_taller.infrastructure.persistence.adapter;

import com.taller.gestion_taller.infrastructure.persistence.entity.ContadorFacturaEntity;
import com.taller.gestion_taller.infrastructure.persistence.repository.JpaContadorFacturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContadorFacturaRepositoryAdapter")
class ContadorFacturaRepositoryAdapterTest {

    private static final Long ID = 1L;

    @Mock
    private JpaContadorFacturaRepository jpaRepository;

    @InjectMocks
    private ContadorFacturaRepositoryAdapter adapter;

    @Test
    @DisplayName("Debe incrementar y devolver el siguiente numero cuando la fila del contador ya existe")
    void debeIncrementarNumeroCuandoLaFilaYaExiste() {
        ContadorFacturaEntity contadorExistente = ContadorFacturaEntity.builder().id(ID).ultimoNumero(5L).build();

        when(jpaRepository.findConLockById(ID)).thenReturn(Optional.of(contadorExistente));
        when(jpaRepository.save(any(ContadorFacturaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long resultado = adapter.siguienteNumero();

        assertThat(resultado).isEqualTo(6L);
        verify(jpaRepository).save(contadorExistente);
        verify(jpaRepository, times(1)).findConLockById(ID);
    }

    @Test
    @DisplayName("Debe crear el contador inicial cuando la fila todavia no existe")
    void debeCrearContadorInicialCuandoLaFilaNoExiste() {
        when(jpaRepository.findConLockById(ID)).thenReturn(Optional.empty());
        when(jpaRepository.save(any(ContadorFacturaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long resultado = adapter.siguienteNumero();

        assertThat(resultado).isEqualTo(1L);
        verify(jpaRepository, times(1)).findConLockById(ID);
        verify(jpaRepository, times(2)).save(any(ContadorFacturaEntity.class));
    }
}
