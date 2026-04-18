package com.taller.gestion_taller.application.usecases.modelo;

import com.taller.gestion_taller.domain.model.Modelo;
import com.taller.gestion_taller.domain.repositories.ModeloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarModelosUseCase")
class ListarModelosUseCaseTest {

    @Mock private ModeloRepository modeloRepository;

    @InjectMocks
    private ListarModelosUseCase useCase;

    @Test
    @DisplayName("debe listar todos los modelos cuando no hay filtro por marca")
    void debeListarTodosLosModelos() {
        Modelo m1 = mock(Modelo.class);
        Modelo m2 = mock(Modelo.class);
        when(modeloRepository.findAll()).thenReturn(List.of(m1, m2));

        List<Modelo> resultado = useCase.listar(Optional.empty());

        assertThat(resultado).hasSize(2).containsExactly(m1, m2);
        verify(modeloRepository).findAll();
    }

    @Test
    @DisplayName("debe listar modelos por marca")
    void debeListarModelosPorMarca() {
        Long marcaId = 1L;
        Modelo m1 = mock(Modelo.class);
        when(modeloRepository.findByMarcaId(marcaId)).thenReturn(List.of(m1));

        List<Modelo> resultado = useCase.listar(Optional.of(marcaId));

        assertThat(resultado).hasSize(1).containsExactly(m1);
        verify(modeloRepository).findByMarcaId(marcaId);
    }
}
