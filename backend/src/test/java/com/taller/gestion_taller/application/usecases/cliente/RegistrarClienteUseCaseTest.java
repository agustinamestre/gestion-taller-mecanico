package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrarClienteUseCase")
class RegistrarClienteUseCaseTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteApplicationMapper clienteApplicationMapper;

    @InjectMocks
    private RegistrarClienteUseCase useCase;

    // Datos de prueba reutilizables
    private RegistrarClienteCommand commandValido;
    private Cliente clienteSinId;
    private Cliente clienteGuardado;

    @BeforeEach
    void setUp() {
        // Command de entrada
        commandValido = new RegistrarClienteCommand(
                "12345678",
                "Juan",
                "Perez",
                "3364249176",
                "juan.perez@gmail.com",
                "Calle Falsa 123"
        );

        // Cliente base sin ID (antes de guardar)
        clienteSinId = Cliente.builder()
                .dni("12345678")
                .nombre("Juan")
                .apellido("Perez")
                .telefono("3364249176")
                .email("juan.perez@gmail.com")
                .direccion("Calle Falsa 123")
                .activo(true)
                .fechaCreacion(LocalDate.now())
                .fechaModificacion(LocalDate.now())
                .build();

        // Cliente con ID
        clienteGuardado = clienteSinId.toBuilder()
                .id(1L)
                .build();
    }


    @Test
    @DisplayName("Registrar cliente con todos los campos")
    void debeRegistrarClienteExitosamente() {

        when(clienteApplicationMapper.commandToDomain(commandValido)).thenReturn(clienteSinId);
        when(clienteRepository.findByDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteSinId)).thenReturn(clienteGuardado);

        Cliente resultado = useCase.registrarCliente(commandValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getDni()).isEqualTo("12345678");
        assertThat(resultado.getNombre()).isEqualTo("Juan");
        assertThat(resultado.getApellido()).isEqualTo("Perez");
        assertThat(resultado.isActivo()).isTrue();
        verify(clienteApplicationMapper, times(1)).commandToDomain(commandValido);

    }

    @Test
    @DisplayName("Registrar cliente sin email")
    void debeRegistrarClienteSinEmail() {

        RegistrarClienteCommand commandSinEmail = new RegistrarClienteCommand(
                "12345678", "Juan", "Perez", "3364249176", "", "Calle Falsa 123"
        );

        Cliente clienteSinEmail = clienteSinId.toBuilder()
                .email("")
                .build();

        Cliente clienteGuardadoSinEmail = clienteSinEmail.toBuilder()
                .id(1L)
                .build();

        when(clienteApplicationMapper.commandToDomain(commandSinEmail)).thenReturn(clienteSinEmail);
        when(clienteRepository.findByDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteSinEmail)).thenReturn(clienteGuardadoSinEmail);

        Cliente resultado = useCase.registrarCliente(commandSinEmail);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEmpty();
    }

    @Test
    @DisplayName("Cliente sin dirección")
    void debeManejarClienteSinDireccion() {

        RegistrarClienteCommand commandSinDireccion = new RegistrarClienteCommand(
                "12345678", "Juan", "Perez", "3364249176", "juan@gmail.com", ""
        );

        Cliente clienteSinDireccion = clienteSinId.toBuilder()
                .direccion("")
                .build();

        Cliente clienteGuardadoSinDireccion = clienteSinDireccion.toBuilder()
                .id(1L)
                .build();

        when(clienteApplicationMapper.commandToDomain(commandSinDireccion)).thenReturn(clienteSinDireccion);
        when(clienteRepository.findByDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteSinDireccion)).thenReturn(clienteGuardadoSinDireccion);

        Cliente resultado = useCase.registrarCliente(commandSinDireccion);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getDireccion()).isEmpty();
    }

    @Test
    @DisplayName("Verificar que el DNI no exista antes de guardar")
    void debeVerificarDniAntesDeGuardar() {
        when(clienteApplicationMapper.commandToDomain(commandValido)).thenReturn(clienteSinId);
        when(clienteRepository.findByDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepository.save(clienteSinId)).thenReturn(clienteGuardado);

        useCase.registrarCliente(commandValido);

        verify(clienteRepository, times(1)).findByDni("12345678");
    }


    @Test
    @DisplayName("Lanzar excepción cuando el DNI ya existe")
    void debeLanzarExcepcionCuandoDniYaExiste() {
        Cliente clienteExistente = Cliente.builder()
                .id(99L)
                .dni("12345678")
                .build();

        when(clienteApplicationMapper.commandToDomain(commandValido)).thenReturn(clienteSinId);
        when(clienteRepository.findByDni("12345678")).thenReturn(Optional.of(clienteExistente));

        Exception exception = assertThrows(BusinessRunTimeException.class, () -> {
            useCase.registrarCliente(commandValido);
        });

        assertTrue(exception.getMessage().contains("Ya existe un cliente con DNI: 12345678"));;
    }
}
