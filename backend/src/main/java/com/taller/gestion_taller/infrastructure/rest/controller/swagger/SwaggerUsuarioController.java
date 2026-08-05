package com.taller.gestion_taller.infrastructure.rest.controller.swagger;

import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.ModificarUsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.UsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema.")
public interface SwaggerUsuarioController {

    @Operation(summary = "Registrar un nuevo usuario.",
            description = "Solo accesible por ADMIN. Crea un nuevo empleado en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UsuarioResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o username ya existe.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "403", description = "Sin permisos de administrador.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PostMapping
    ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioRequest request);

    @Operation(summary = "Listar todos los usuarios.",
            description = "Solo accesible por ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class)))),
                    @ApiResponse(responseCode = "403", description = "Sin permisos de administrador.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @GetMapping
    ResponseEntity<List<UsuarioResponse>> obtenerTodos();

    @Operation(summary = "Obtener usuario por ID.",
            description = "Solo accesible por ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UsuarioResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "403", description = "Sin permisos de administrador.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @GetMapping("/{id}")
    ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id);

    @Operation(summary = "Modificar datos de un usuario.",
            description = "Solo accesible por ADMIN. Permite cambiar nombre, apellido y rol.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario modificado exitosamente.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UsuarioResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "403", description = "Sin permisos de administrador.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @PutMapping("/{id}")
    ResponseEntity<UsuarioResponse> modificar(@PathVariable Long id,
                                              @Valid @RequestBody ModificarUsuarioRequest request);

    @Operation(summary = "Desactivar un usuario.",
            description = "Solo accesible por ADMIN. Baja lógica — el usuario no puede loguearse más.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente."),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "El usuario ya estaba desactivado.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "403", description = "Sin permisos de administrador.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @DeleteMapping("/{id}/desactivar")
    ResponseEntity<Void> desactivar(@PathVariable Long id);

    @Operation(summary = "Obtener perfil propio.",
            description = "Accesible para cualquier usuario autenticado. Devuelve los datos del usuario del token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UsuarioResponse.class)))
            })
    @GetMapping("/me")
    ResponseEntity<UsuarioResponse> obtenerPerfilPropio(Authentication authentication);
}
