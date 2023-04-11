package com.mangareader.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.TimeoutException;

@RestControllerAdvice
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

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, NativeWebRequest request) {
        String error = "Access denied.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.FORBIDDEN, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({DataAlreadyExistsException.class})
    public ResponseEntity<Object> handleDataAlreadyExistsException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "Data is already existed.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.CONFLICT, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "Bad request.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<Object> handleStorageException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "File is empty.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({FileSizeLimitExceededException.class})
    public ResponseEntity<Object> handleFileSizeLimitExceededException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "File size is exceeded limit size.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "Bad credential for JWT.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.UNAUTHORIZED, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({TimeoutException.class})
    public ResponseEntity<Object> handleTimeOutException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "Time out for access resource.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.REQUEST_TIMEOUT, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

    @ExceptionHandler({InterruptedException.class})
    public ResponseEntity<Object> handleInterruptException(
            RuntimeException ex, NativeWebRequest request) {
        String error = "Thread interrupt.";
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.CONFLICT, error, ex.getMessage());
        return new ResponseEntity<>(
                errorDetails, new HttpHeaders(), errorDetails.getStatus());
    }

}
