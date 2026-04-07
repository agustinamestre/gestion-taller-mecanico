package com.taller.gestion_taller.application.usecases.marca;

import com.taller.gestion_taller.domain.model.Marca;
import com.taller.gestion_taller.domain.repositories.MarcaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListarMarcasUseCase")
class ListarMarcasUseCaseTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private ListarMarcasUseCase useCase;

    @Test
    @DisplayName("Listar todas las marcas")
    void debeListarMarcasExitosamente() {
        Marca marca1 = mock(Marca.class);
        Marca marca2 = mock(Marca.class);
        List<Marca> marcas = List.of(marca1, marca2);

        when(marcaRepository.findAll()).thenReturn(marcas);

        List<Marca> resultado = useCase.listarMarcas();

        assertThat(resultado).isEqualTo(marcas);
        verify(marcaRepository).findAll();
        assertThat(useCase.listarMarcas()).hasSize(2);
    }
}
