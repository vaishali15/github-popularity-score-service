package com.challenge.github.popularityscore.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

import java.util.stream.Collectors;

/**
 * Maps common exceptions to ProblemDetail responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("One or more fields are invalid.");
        problemDetail.setTitle("Validation failed");
        problemDetail.setProperty("errors", ex.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                fe -> fe.getDefaultMessage() == null ? "Validation error" : fe.getDefaultMessage(),
                (a, b) -> a
            )));
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleParamValidation(ConstraintViolationException ex) {
        var problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle("Constraint violation");
        problemDetail.setDetail("Request parameters are invalid.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ProblemDetail> handleUpstream(RestClientResponseException ex) {
        var problemDetail = ProblemDetail.forStatus(ex.getStatusCode());
        problemDetail.setTitle("Upstream error");
        problemDetail.setDetail(ex.getStatusText());
        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
        var problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("Unexpected error.");
        return ResponseEntity.internalServerError().body(problemDetail);
    }

}
