package com.taller.gestion_taller.infrastructure.rest.controller;

import com.taller.gestion_taller.infrastructure.rest.dto.auth.request.LoginRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.auth.response.LoginResponse;
import com.taller.gestion_taller.infrastructure.security.JwtService;
import com.taller.gestion_taller.infrastructure.security.UsuarioDetails;
import com.taller.gestion_taller.infrastructure.security.UsuarioDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UsuarioDetails userDetails = (UsuarioDetails) usuarioDetailsService
                .loadUserByUsername(request.username());

        String token = jwtService.generarToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(
                token,
                userDetails.getUsername(),
                userDetails.getUsuario().getRol().name()
        ));
    }
}