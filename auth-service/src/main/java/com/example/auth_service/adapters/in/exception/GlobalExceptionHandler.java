package com.example.auth_service.adapters.in.exception;

import com.example.auth_service.application.dto.security.ApiError;
import com.example.auth_service.domain.exception.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    // Domain exception
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ApiError> handleObjectNotFound(
            HttpServletRequest request, ObjectNotFoundException ex) {

        ApiError apiError = ApiError.builder()
                .backendMessage(ex.getMessage())
                .url(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // Validation exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Illegal argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            HttpServletRequest request, IllegalArgumentException ex) {

        ApiError apiError = ApiError.builder()
                .backendMessage(ex.getMessage())
                .url(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .message("Parámetros de entrada inválidos")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // Generic exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            HttpServletRequest request, Exception ex) {

        log.error("Error interno", ex);

        ApiError apiError = ApiError.builder()
                .backendMessage(ex.getMessage())
                .url(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .message("Error interno del servidor")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
