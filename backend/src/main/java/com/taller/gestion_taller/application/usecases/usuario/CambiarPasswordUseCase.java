package com.taller.gestion_taller.application.usecases.usuario;

import com.taller.gestion_taller.application.command.usuario.CambiarPasswordCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.domain.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CambiarPasswordUseCase implements CambiarPassword {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void cambiar(CambiarPasswordCommand command) {
        Usuario usuario = usuarioRepository.findByUsername(command.username())
                .orElseThrow(() -> new NotFoundException(
                        BusinessErrors.usuarioNoEncontrado(command.username())));

        if (!passwordEncoder.matches(command.passwordActual(), usuario.getPassword())) {
            throw new BusinessRunTimeException(BusinessErrors.passwordActualIncorrecta());
        }

        Usuario modificado = usuario.toBuilder()
                .password(passwordEncoder.encode(command.passwordNueva()))
                .build();

        usuarioRepository.save(modificado);
    }
}
