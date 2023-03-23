package com.mangareader.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex) {

        String error = "Resource not found.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.NOT_FOUND, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        String error = "Access denied.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.FORBIDDEN, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({DataAlreadyExistsException.class})
    public ResponseEntity<Object> handleDataAlreadyExistsException(
            Exception ex, WebRequest request) {
        String error = "Data is already existed.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.CONFLICT, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(
            Exception ex, WebRequest request) {
        String error = "Bad request.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<Object> handleStorageException(
            Exception ex, WebRequest request) {
        String error = "File is empty.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

}
