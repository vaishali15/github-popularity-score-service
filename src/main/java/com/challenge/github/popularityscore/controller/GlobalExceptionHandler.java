package com.challenge.github.popularityscore.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Maps common exceptions to ProblemDetail responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleParamValidation(ConstraintViolationException ex) {
        log.error("ConstraintViolationException:", ex);
        var problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle("Constraint violation");
        problemDetail.setDetail("Request parameters are invalid.");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {
        log.error("Something went wrong:", ex);
        var problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.internalServerError().body(problemDetail);
    }

}
