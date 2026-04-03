package com.taller.gestion_taller.infrastructure.rest.exception.advice;

import com.taller.gestion_taller.domain.exception.BusinessError;
import com.taller.gestion_taller.domain.exception.NotFoundException;
import com.taller.gestion_taller.infrastructure.rest.exception.ApiError;
import com.taller.gestion_taller.infrastructure.rest.exception.ApiErrorResponse;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String INVALIDO = "_invalido";
    public static final String REQUERIDO = "_requerido";
    public static final String CODE_ERROR_TECNICO = "ERROR_TECNICO";
    public static final String CAMPO_REQUERIDO = "El campo {0} es requerido";
    public static final String CAMPO_CON_VALOR_INVALIDO_CUSTOM = "El campo {0} tiene un valor invalido: {1}";

    // ERRORES GENERICOS (catch-all)

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiErrorResponse globalExceptionHandler(Exception ex, ServletWebRequest request) {
        log.error("**** ERROR TECNICO - EXCEPTION - Descripcion: {}", ex.getMessage());
        var apiError = new ApiError(CODE_ERROR_TECNICO, getGenericErrorMessageAndCause(ex), ex.getMessage());
        return new ApiErrorResponse(request.getRequest().getRequestURI(), List.of(apiError));
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

    // UTILS

    private ApiError toApiError(BusinessError businessError) {
        return new ApiError(businessError.code(), businessError.message(), businessError.logDetail());
    }

    private String getGenericErrorMessageAndCause(Exception ex) {
        if (ex.getCause() != null) {
            return ex.getCause().toString() + ex.getMessage();
        }
        log.trace("MENSAJE_PILA_DE_LLAMADAS", ex);
        return "Exception: " + ex.getMessage();
    }
}
