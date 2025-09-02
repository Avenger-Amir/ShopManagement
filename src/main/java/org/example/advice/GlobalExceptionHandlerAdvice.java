package org.example.advice;

import jakarta.ws.rs.WebApplicationException;
import java.util.List;

import org.example.exceptions.ShopMicroserviceWebApplicationException;
import org.example.exceptions.WebApplicationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(ShopMicroserviceWebApplicationException.class)
    public ResponseEntity<List<WebApplicationError>> handleGenericException(
            final ShopMicroserviceWebApplicationException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.valueOf(exception.getResponse().getStatus()))
                .body(exception.getErrors());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(final Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(
            final DataIntegrityViolationException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(WebApplicationException.class)
    public ResponseEntity<String> handleWebApplicationException(
            final WebApplicationException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.valueOf(exception.getResponse().getStatus()))
                .body(exception.getMessage());
    }
}

