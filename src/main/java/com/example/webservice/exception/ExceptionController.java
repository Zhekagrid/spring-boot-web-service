package com.example.webservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    private static final String ERRORS_KEY = "errors";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> errorDescription(x.getField(), x.getDefaultMessage())).collect(Collectors.toList());
        body.put(ERRORS_KEY, errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String errorDescription = errorDescription(violation.getPropertyPath(), violation.getMessage());
            errors.add(errorDescription);
        }
        Map<String, List<String>> body = new HashMap<>();
        body.put(ERRORS_KEY, errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNonAuthenticatedException.class})
    public ResponseEntity<Object> handleUserNonAuthenticated(UserNonAuthenticatedException ex) {

        Map<String, String> body = new HashMap<>();
        body.put(ERRORS_KEY, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    private String errorDescription(String field, String message) {
        return (field != null ? field : EMPTY_STRING) + SPACE + (message != null ? message : EMPTY_STRING);
    }

    private String errorDescription(Path propertyPath, String message) {
        return (propertyPath != null ? propertyPath : EMPTY_STRING) + SPACE + (message != null ? message : EMPTY_STRING);
    }
}
