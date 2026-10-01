package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.application.command.auth.RefrescarTokenCommand;
import com.taller.gestion_taller.application.dto.TokensRenovados;
import com.taller.gestion_taller.infrastructure.rest.dto.auth.request.LoginRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.auth.request.RefrescarTokenRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.auth.response.LoginResponse;
import com.taller.gestion_taller.infrastructure.rest.dto.auth.response.RefrescarTokenResponse;
import com.taller.gestion_taller.infrastructure.security.jwt.JwtService;
import com.taller.gestion_taller.infrastructure.security.userdetails.UsuarioDetails;
import com.taller.gestion_taller.infrastructure.security.userdetails.UsuarioDetailsService;
import com.taller.gestion_taller.infrastructure.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UsuarioDetails userDetails = (UsuarioDetails) usuarioDetailsService
                .loadUserByUsername(request.username());

        String token = jwtService.generarToken(userDetails);
        String refreshToken = authService.generarRefreshToken(userDetails.getUsuario().getId());

        return ResponseEntity.ok(new LoginResponse(
                token,
                refreshToken,
                userDetails.getUsername(),
                userDetails.getUsuario().getRol().name()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefrescarTokenResponse> refresh(@Valid @RequestBody RefrescarTokenRequest request) {
        TokensRenovados tokens = authService.refrescarToken(new RefrescarTokenCommand(request.refreshToken()));
        return ResponseEntity.ok(new RefrescarTokenResponse(tokens.accessToken(), tokens.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
        authService.logout(userDetails.getUsuario().getId());
        return ResponseEntity.noContent().build();
    }
}