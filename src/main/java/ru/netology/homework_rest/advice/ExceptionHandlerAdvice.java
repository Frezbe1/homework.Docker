package ru.netology.homework_rest.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.homework_rest.error.ValidationErrorResponse;
import ru.netology.homework_rest.error.Violation;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<String> invalid(InvalidCredentials e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedUser.class)
    public ResponseEntity<String> unauthorized(UnauthorizedUser e) {
        System.out.printf("[unauthorized]:%s\n", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onBindException(BindException e) {
        final List<Violation> violations = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(
                        x -> new Violation(
                                x.getField(),
                                x.getDefaultMessage()
                        )
                )
                .collect(Collectors.toList());
        System.out.printf("[onBindExceptionviolations]:%s\n", violations);
        return new ValidationErrorResponse(violations);
    }

}
