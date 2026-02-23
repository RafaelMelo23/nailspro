package com.rafael.nailspro.webapp.infrastructure.exception;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> business(BusinessException e, HttpServletRequest request) {
        String error = "Erro de validação";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ProfessionalBusyException.class)
    public ResponseEntity<StandardError> busy(ProfessionalBusyException e, HttpServletRequest request) {
        String error = "Erro de validação";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericUnknownError(Exception e, HttpServletRequest request) {
        String error = "Erro interno do servidor";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        Sentry.captureException(e);

        log.error("Internal server error", e);
        StandardError err = new StandardError(Instant.now(),
                status.value(),
                error,
                "Ocorreu um erro inesperado. Contate o suporte.",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<StandardError> userAlreadyExists(Exception e, HttpServletRequest request) {
        String error = "Usuário já cadastrado";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        log.error("User tried registering with existing user info", e);
        StandardError err = new StandardError(Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
