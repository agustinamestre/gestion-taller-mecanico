package com.taller.gestion_taller.application.usecases.auth;

import com.taller.gestion_taller.application.command.auth.RefrescarTokenCommand;
import com.taller.gestion_taller.domain.exception.UnauthorizedException;
import com.taller.gestion_taller.domain.model.RefreshToken;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.RefreshTokenRepository;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import com.taller.gestion_taller.infrastructure.security.jwt.JwtService;
import com.taller.gestion_taller.infrastructure.security.refresh.RefreshTokenHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefrescarTokenUseCase")
class RefrescarTokenUseCaseTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RefreshTokenHasher refreshTokenHasher;

    @Mock
    private GenerarRefreshToken generarRefreshToken;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefrescarTokenUseCase useCase;

    @Test
    @DisplayName("Rechaza renovar la sesion de un usuario desactivado")
    void rechazaUsuarioDesactivado() {
        RefreshToken token = RefreshToken.crearNuevo(1L, "hash", Instant.now().plus(1, ChronoUnit.DAYS));
        Usuario desactivado = Usuario.builder().id(1L).username("jperez").activo(false).build();

        when(refreshTokenHasher.hashear("plano")).thenReturn("hash");
        when(refreshTokenRepository.findByTokenHash("hash")).thenReturn(Optional.of(token));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(desactivado));

        assertThrows(UnauthorizedException.class,
                () -> useCase.refrescar(new RefrescarTokenCommand("plano")));

        verify(jwtService, never()).generarToken(any());
        verify(generarRefreshToken, never()).generar(any());
    }
}
