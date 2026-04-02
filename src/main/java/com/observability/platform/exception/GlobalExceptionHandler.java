package com.observability.platform.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(String error, List<String> details) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception){
        List<String> details = exception.getBindingResult()
                .getFieldErrors()                        // list of field errors
                .stream()
                .map(FieldError::getDefaultMessage)      // extract the message
                .toList();                                // collect to list

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Validation failed", details));

        /*
         * Example of error response:
         * {
         *   "error": "Validation failed",
         *   "details": [
         *     "must not be blank",
         *     "must not be null"
         *   ]
         * }
         */
    }
}
