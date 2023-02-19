package com.mangareader.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex) {

        String error = "Resource not found.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.NOT_FOUND, ex.getMessage(), error);
        return new ResponseEntity<Object>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        String error = "Access denied.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.FORBIDDEN, ex.getMessage(), error);
        return new ResponseEntity<Object>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({DataAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataAlreadyExistsException(
            Exception ex, WebRequest request) {
        String error = "Data is already existed.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.CONFLICT, ex.getMessage(), error);
        return new ResponseEntity<Object>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }
}
