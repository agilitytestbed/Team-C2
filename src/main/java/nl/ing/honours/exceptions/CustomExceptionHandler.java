package nl.ing.honours.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {InvalidInputException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, code = HttpStatus.METHOD_NOT_ALLOWED, reason = "Invalid input given.")
    public void handleInvalidInputException() {
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, code = HttpStatus.NOT_FOUND, reason = "Resource not found.")
    public void handleResourceNotFoundException() {
    }
}
