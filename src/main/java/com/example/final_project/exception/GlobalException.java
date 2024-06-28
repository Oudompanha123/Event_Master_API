package com.example.final_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Not Found");
        problemDetail.setProperty("dateTime", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
        problemDetail.setTitle("Unauthorized");
        problemDetail.setProperty("dateTime", LocalDateTime.now());
        return problemDetail;
    }


    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequestException(BadRequestException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("dateTime", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");

        problemDetail.setProperty("Errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        // Iterate over all validation results
        for (var parameterError : ex.getAllValidationResults()) {
            // Get parameter name
            String parameterName = parameterError.getMethodParameter().getParameterName();

            // Iterate over all resolvable errors for the parameter
            for (var error : parameterError.getResolvableErrors()) {
                // Check if error is an instance of FieldError for detailed field information
                if (error instanceof FieldError fieldError) {
                    String fieldName = fieldError.getField();
                    String errorMessage = fieldError.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                } else {
                    errors.put(parameterName, error.getDefaultMessage());
                }
            }
        }

        // Create and return ProblemDetail object
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("Errors", errors);

        return problemDetail;
    }

}
