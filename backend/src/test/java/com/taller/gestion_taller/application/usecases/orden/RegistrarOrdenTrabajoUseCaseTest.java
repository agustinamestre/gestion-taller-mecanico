//package com.taller.gestion_taller.application.usecases.orden;
//
//import com.taller.gestion_taller.application.command.orden.RegistrarOrdenTrabajoCommand;
//import com.taller.gestion_taller.application.mapper.OrdenTrabajoApplicationMapper;
//import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
//import com.taller.gestion_taller.domain.exception.NotFoundException;
//import com.taller.gestion_taller.domain.model.EstadoPresupuesto;
//import com.taller.gestion_taller.domain.model.OrdenTrabajo;
//import com.taller.gestion_taller.domain.model.Presupuesto;
//import com.taller.gestion_taller.domain.model.Vehiculo;
//import com.taller.gestion_taller.domain.repositories.OrdenTrabajoRepository;
//import com.taller.gestion_taller.domain.repositories.PresupuestoRepository;
//import com.taller.gestion_taller.domain.repositories.VehiculoRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("RegistrarOrdenTrabajoUseCase")
//class RegistrarOrdenTrabajoUseCaseTest {
//
//    private static final String PATENTE = "ABC123";
//    private static final Long USUARIO_ID = 1L;
//    private static final Long PRESUPUESTO_ID = 1L;
//    private static final String DESCRIPCION = "Ruido en el motor";
//
//    private static final RegistrarOrdenTrabajoCommand COMMAND_CON_PRESUPUESTO =
//            new RegistrarOrdenTrabajoCommand(PATENTE, PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);
//
//    private static final RegistrarOrdenTrabajoCommand COMMAND_SIN_PRESUPUESTO =
//            new RegistrarOrdenTrabajoCommand(PATENTE, null, DESCRIPCION, USUARIO_ID);
//
//    @Mock private OrdenTrabajoRepository ordenTrabajoRepository;
//    @Mock private VehiculoRepository vehiculoRepository;
//    @Mock private PresupuestoRepository presupuestoRepository;
//    @Mock private OrdenTrabajoApplicationMapper ordenTrabajoMapper;
//
//    @InjectMocks
//    private RegistrarOrdenTrabajoUseCase useCase;
//
//    private OrdenTrabajo orden;
//
//    @BeforeEach
//    void setUp() {
//        orden = mock(OrdenTrabajo.class);
//    }
//
//    @Nested
//    @DisplayName("Cuando se registra con presupuesto")
//    class ConPresupuesto {
//
//        @Test
//        @DisplayName("debe registrar la orden cuando el presupuesto esta APROBADO y tiene vehiculo")
//        void registraOrdenConPresupuestoAprobado() {
//            Vehiculo vehiculo = mock(Vehiculo.class);
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(vehiculo);
//            when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.APROBADO);
//            when(vehiculo.getPatente()).thenReturn(PATENTE);
//            when(ordenTrabajoRepository.save(orden)).thenReturn(orden);
//
//            OrdenTrabajo resultado = useCase.registrar(COMMAND_CON_PRESUPUESTO);
//
//            assertThat(resultado).isEqualTo(orden);
//            verify(orden).setPresupuesto(presupuesto);
//            verify(orden).setVehiculo(vehiculo);
//            verify(ordenTrabajoRepository).save(orden);
//            verifyNoInteractions(vehiculoRepository);
//        }
//
//        @Test
//        @DisplayName("debe registrar la orden cuando la patente del comando difiere solo en mayusculas/minusculas")
//        void registraOrdenConPatenteDistintaCase() {
//            RegistrarOrdenTrabajoCommand commandLowerCase =
//                    new RegistrarOrdenTrabajoCommand("abc123", PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);
//            Vehiculo vehiculo = mock(Vehiculo.class);
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(commandLowerCase)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(vehiculo);
//            when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.APROBADO);
//            when(vehiculo.getPatente()).thenReturn(PATENTE);
//            when(ordenTrabajoRepository.save(orden)).thenReturn(orden);
//
//            useCase.registrar(commandLowerCase);
//
//            verify(ordenTrabajoRepository).save(orden);
//        }
//
//        @Test
//        @DisplayName("debe registrar la orden cuando solo se manda el presupuestoId (sin patente)")
//        void registraOrdenSinPatenteEnComando() {
//            RegistrarOrdenTrabajoCommand commandSinPatente =
//                    new RegistrarOrdenTrabajoCommand(null, PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);
//            Vehiculo vehiculo = mock(Vehiculo.class);
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(commandSinPatente)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(vehiculo);
//            when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.APROBADO);
//            when(ordenTrabajoRepository.save(orden)).thenReturn(orden);
//
//            useCase.registrar(commandSinPatente);
//
//            verify(orden).setVehiculo(vehiculo);
//            verify(ordenTrabajoRepository).save(orden);
//        }
//
//        @Test
//        @DisplayName("H5: debe lanzar BusinessRunTimeException cuando la patente del comando NO coincide con la del presupuesto")
//        void lanzaExcepcionCuandoPatenteInconsistente() {
//            RegistrarOrdenTrabajoCommand commandPatenteDistinta =
//                    new RegistrarOrdenTrabajoCommand("XYZ999", PRESUPUESTO_ID, DESCRIPCION, USUARIO_ID);
//            Vehiculo vehiculo = mock(Vehiculo.class);
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(commandPatenteDistinta)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(vehiculo);
//            when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.APROBADO);
//            when(vehiculo.getPatente()).thenReturn(PATENTE);
//
//            assertThatThrownBy(() -> useCase.registrar(commandPatenteDistinta))
//                    .isInstanceOf(BusinessRunTimeException.class)
//                    .extracting("businessError.code")
//                    .isEqualTo("PATENTE_INCONSISTENTE_CON_PRESUPUESTO");
//
//            verify(orden, never()).setPresupuesto(any());
//            verify(orden, never()).setVehiculo(any());
//            verify(ordenTrabajoRepository, never()).save(any());
//            verifyNoInteractions(vehiculoRepository);
//        }
//
//        @Test
//        @DisplayName("debe lanzar NotFoundException cuando el presupuesto no existe")
//        void lanzaNotFoundCuandoPresupuestoNoExiste() {
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.empty());
//
//            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
//                    .isInstanceOf(NotFoundException.class);
//
//            verify(ordenTrabajoRepository, never()).save(any());
//            verifyNoInteractions(vehiculoRepository);
//        }
//
//        @Test
//        @DisplayName("debe lanzar BusinessRunTimeException cuando el presupuesto no tiene vehiculo asociado")
//        void lanzaExcepcionCuandoPresupuestoSinVehiculo() {
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(null);
//
//            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
//                    .isInstanceOf(BusinessRunTimeException.class)
//                    .hasMessageContaining("vehículo");
//
//            verify(orden, never()).setPresupuesto(any());
//            verify(orden, never()).setVehiculo(any());
//            verify(ordenTrabajoRepository, never()).save(any());
//            verifyNoInteractions(vehiculoRepository);
//        }
//
//        @Test
//        @DisplayName("debe lanzar BusinessRunTimeException cuando el presupuesto no esta APROBADO")
//        void lanzaExcepcionCuandoPresupuestoNoAprobado() {
//            Vehiculo vehiculo = mock(Vehiculo.class);
//            Presupuesto presupuesto = mock(Presupuesto.class);
//
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_CON_PRESUPUESTO)).thenReturn(orden);
//            when(presupuestoRepository.findById(PRESUPUESTO_ID)).thenReturn(Optional.of(presupuesto));
//            when(presupuesto.getVehiculo()).thenReturn(vehiculo);
//            when(presupuesto.getEstado()).thenReturn(EstadoPresupuesto.PENDIENTE);
//
//            assertThatThrownBy(() -> useCase.registrar(COMMAND_CON_PRESUPUESTO))
//                    .isInstanceOf(BusinessRunTimeException.class);
//
//            verify(orden, never()).setPresupuesto(any());
//            verify(ordenTrabajoRepository, never()).save(any());
//            verifyNoInteractions(vehiculoRepository);
//        }
//    }
//
//    @Nested
//    @DisplayName("Cuando se registra sin presupuesto")
//    class SinPresupuesto {
//
//        @Test
//        @DisplayName("debe registrar la orden buscando el vehiculo por patente")
//        void registraOrdenPorPatente() {
//            Vehiculo vehiculo = mock(Vehiculo.class);
//
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_SIN_PRESUPUESTO)).thenReturn(orden);
//            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.of(vehiculo));
//            when(ordenTrabajoRepository.save(orden)).thenReturn(orden);
//
//            OrdenTrabajo resultado = useCase.registrar(COMMAND_SIN_PRESUPUESTO);
//
//            assertThat(resultado).isEqualTo(orden);
//            verify(orden).setVehiculo(vehiculo);
//            verify(orden, never()).setPresupuesto(any());
//            verify(ordenTrabajoRepository).save(orden);
//            verifyNoInteractions(presupuestoRepository);
//        }
//
//        @Test
//        @DisplayName("debe lanzar NotFoundException cuando el vehiculo no existe")
//        void lanzaNotFoundCuandoVehiculoNoExiste() {
//            when(ordenTrabajoMapper.commandToDomain(COMMAND_SIN_PRESUPUESTO)).thenReturn(orden);
//            when(vehiculoRepository.findByPatente(PATENTE)).thenReturn(Optional.empty());
//
//            assertThatThrownBy(() -> useCase.registrar(COMMAND_SIN_PRESUPUESTO))
//                    .isInstanceOf(NotFoundException.class);
//
//            verify(orden, never()).setVehiculo(any());
//            verify(ordenTrabajoRepository, never()).save(any());
//            verifyNoInteractions(presupuestoRepository);
//        }
//    }
//}
