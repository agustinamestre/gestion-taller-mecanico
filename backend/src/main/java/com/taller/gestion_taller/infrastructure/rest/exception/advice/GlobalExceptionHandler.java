package com.taller.gestion_taller.infrastructure.rest.exception.advice;

import com.taller.gestion_taller.domain.exception.BusinessError;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.domain.exception.UnauthorizedException;
import com.taller.gestion_taller.infrastructure.rest.exception.ApiError;
import com.taller.gestion_taller.infrastructure.rest.exception.ApiErrorResponse;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String INVALIDO = "_invalido";
    public static final String REQUERIDO = "_requerido";
    public static final String CODE_ERROR_TECNICO = "ERROR_TECNICO";
    public static final String CAMPO_REQUERIDO = "{0} requerido";
    public static final String CAMPO_CON_VALOR_INVALIDO_CUSTOM = "{0} invalido: {1}";
    public static final String MENSAJE_ERROR_TECNICO = "Ocurrió un error inesperado. Intentá nuevamente más tarde.";

    // ERRORES GENERICOS (catch-all)

    // El detalle queda solo en el log: al cliente no se le exponen clases, mensajes ni SQL internos.
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrorResponse globalExceptionHandler(Exception ex, ServletWebRequest request) {
        log.error("**** ERROR TECNICO - {} {}", request.getRequest().getMethod(), request.getRequest().getRequestURI(), ex);
        var apiError = new ApiError(CODE_ERROR_TECNICO, MENSAJE_ERROR_TECNICO);
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(apiError));
    }

    // JSON MAL FORMADO / VALORES CON TIPO INVALIDO (ej. enums)

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex,
                                                                   ServletWebRequest request) {
        log.warn("[REQUEST INVALIDO] Cuerpo ilegible en {} {}: {}",
                request.getRequest().getMethod(), request.getRequest().getRequestURI(), ex.getMessage());

        ApiError error;
        if (ex.getCause() instanceof MismatchedInputException mismatch && nombreCampo(mismatch) != null) {
            String field = nombreCampo(mismatch);
            error = new ApiError(field + INVALIDO,
                    MessageFormat.format(CAMPO_CON_VALOR_INVALIDO_CUSTOM, field, describirValorEsperado(mismatch.getTargetType())));
        } else {
            error = new ApiError("JSON_MAL_FORMADO", "El cuerpo de la solicitud no es un JSON válido.");
        }
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex,
                                                                       ServletWebRequest request) {
        String field = ex.getName();
        var error = new ApiError(field + INVALIDO,
                MessageFormat.format(CAMPO_CON_VALOR_INVALIDO_CUSTOM, field, describirValorEsperado(ex.getRequiredType())));
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex,
                                                                           ServletWebRequest request) {
        String field = ex.getParameterName();
        var error = new ApiError(field + REQUERIDO, MessageFormat.format(CAMPO_REQUERIDO, field));
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    // CLAVES DUPLICADAS QUE NO SE VALIDARON ANTES (ej. dos requests simultaneos)

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ApiErrorResponse dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex,
                                                                   ServletWebRequest request) {
        log.warn("[CONFLICTO] Violacion de integridad en {} {}: {}",
                request.getRequest().getMethod(), request.getRequest().getRequestURI(), ex.getMostSpecificCause().getMessage());
        var error = new ApiError("DATO_DUPLICADO",
                "Ya existe un registro con esos datos o el dato está en uso. Revisá la información e intentá nuevamente.");
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    // RUTA INEXISTENTE, METODO O CONTENT-TYPE NO SOPORTADO: se respeta el status propio (404/405/415)

    @ExceptionHandler({NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<ApiErrorResponse> errorResponseHandler(Exception ex, ServletWebRequest request) {
        var status = ((ErrorResponse) ex).getStatusCode();
        var error = new ApiError("SOLICITUD_NO_SOPORTADA", "La solicitud no es válida para este recurso.");
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error)));
    }

    // VALIDACIONES DE @Valid EN REQUEST BODY

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex,
                                                                   ServletWebRequest request) {
        final String requestURI = request.getRequest().getRequestURI();
        log.error("[REQUEST INVALIDO] Se interrumpe el procesamiento de la peticion: {} {}",
                request.getRequest().getMethod(), requestURI);

        List<ApiError> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        final String fieldText = fieldError.getField();
                        final String field = fieldText.split("\\.").length > 1
                                ? Arrays.stream(fieldText.split("\\.")).skip(1).findFirst().orElse(fieldText)
                                : fieldText;

                        if (NotNull.class.getSimpleName().equals(error.getCode())) {
                            return new ApiError(field + REQUERIDO, MessageFormat.format(CAMPO_REQUERIDO, field));
                        } else if (NotBlank.class.getSimpleName().equals(error.getCode())) {
                            return new ApiError(field + REQUERIDO, MessageFormat.format(CAMPO_REQUERIDO, field));
                        } else {
                            return new ApiError(field + INVALIDO,
                                    MessageFormat.format(CAMPO_CON_VALOR_INVALIDO_CUSTOM, field, error.getDefaultMessage()));
                        }
                    } else {
                        return new ApiError(error.getCode(), error.getDefaultMessage());
                    }
                })
                .toList();

        return new ApiErrorResponse(requestURI, errors);
    }

    // VALIDACIONES EN @PathVariable / @RequestParam

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse handlerMethodValidationExceptionHandler(HandlerMethodValidationException ex,
                                                                    ServletWebRequest request) {
        final String requestURI = request.getRequest().getRequestURI();
        log.error("[REQUEST INVALIDO] Se interrumpe el procesamiento de la peticion: {} {}",
                request.getRequest().getMethod(), requestURI);

        var errors = ex.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof DefaultMessageSourceResolvable resolvable) {
                        final String field = ((DefaultMessageSourceResolvable) resolvable.getArguments()[0]).getDefaultMessage();
                        return new ApiError(field + INVALIDO,
                                MessageFormat.format(CAMPO_CON_VALOR_INVALIDO_CUSTOM, field, error.getDefaultMessage()));
                    } else {
                        return new ApiError(CODE_ERROR_TECNICO, error.getDefaultMessage());
                    }
                }).toList();

        return new ApiErrorResponse(requestURI, errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrorResponse unauthorizedExceptionHandler(UnauthorizedException ex, ServletWebRequest request) {
        return ApiErrorResponse.crearResponseConError(request.getRequest().getRequestURI(), toApiError(ex.getBusinessError()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = NOT_FOUND)
    public ApiErrorResponse externalNotFoundException(NotFoundException ex, ServletWebRequest request) {
        return ApiErrorResponse.crearResponseConError(request.getRequest().getRequestURI(), toApiError(ex.getBusinessError()));
    }

    @ExceptionHandler(BusinessRunTimeException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse handleBusinessException(BusinessRunTimeException ex, ServletWebRequest request) {
        List<ApiError> apiErrors = ex.getErrors().stream()
                .map(this::toApiError)
                .toList();
        return new ApiErrorResponse(request.getRequest().getRequestURI(), apiErrors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrorResponse badCredentialsExceptionHandler(BadCredentialsException ex,
                                                           ServletWebRequest request) {
        var error = new ApiError("CREDENCIALES_INVALIDAS", "Usuario o contraseña incorrectos.");
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiErrorResponse disabledExceptionHandler(DisabledException ex,
                                                     ServletWebRequest request) {
        var error = new ApiError("USUARIO_INACTIVO", "El usuario está desactivado.");
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(error));
    }

    // UTILS

    private ApiError toApiError(BusinessError businessError) {
        return new ApiError(businessError.code(), businessError.message(), businessError.logDetail());
    }

    private String nombreCampo(JacksonException ex) {
        return ex.getPath().stream()
                .map(JacksonException.Reference::getPropertyName)
                .filter(Objects::nonNull)
                .reduce((primero, segundo) -> segundo)
                .orElse(null);
    }

    private String describirValorEsperado(Class<?> tipo) {
        if (tipo != null && tipo.isEnum()) {
            String permitidos = Arrays.stream(tipo.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return "valores permitidos: " + permitidos;
        }
        return "formato incorrecto";
    }
}
