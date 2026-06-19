package com.dhuaman.presentation.exception;

import com.dhuaman.application.exception.ValidationException;
import com.dhuaman.domain.exception.ConflictException;
import com.dhuaman.domain.exception.DomainException;
import com.dhuaman.domain.exception.NotFoundException;
import com.dhuaman.presentation.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleNotFoundException(NotFoundException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Recurso no encontrado", List.of(ex.getMessage()))));
    }

    @ExceptionHandler(ConflictException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleConflictException(ConflictException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Conflicto de datos", List.of(ex.getMessage()))));
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleValidationException(ValidationException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Error de validación", List.of(ex.getMessage()))));
    }

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleDomainException(DomainException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error("Error de dominio", List.of(ex.getMessage()))));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleGenericException(Exception ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor", List.of())));
    }
}
