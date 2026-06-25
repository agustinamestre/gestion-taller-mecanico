package com.taller.gestion_taller.infrastructure.security;

import com.taller.gestion_taller.infrastructure.rest.exception.ApiError;
import com.taller.gestion_taller.infrastructure.rest.exception.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var error = new ApiError("ACCESO_DENEGADO", "No tiene permisos para acceder a este recurso.");
        var apiErrorResponse = new ApiErrorResponse(request.getRequestURI(), List.of(error));

        try (OutputStream outputStream = response.getOutputStream()) {
            objectMapper.writeValue(outputStream, apiErrorResponse);
            outputStream.flush();
        }
    }
}