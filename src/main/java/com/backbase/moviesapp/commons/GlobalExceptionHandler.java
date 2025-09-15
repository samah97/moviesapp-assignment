package com.backbase.moviesapp.commons;

import com.backbase.moviesapp.dtos.response.ErrorResponse;
import com.backbase.moviesapp.exceptions.MovieNotFoundException;
import com.backbase.moviesapp.exceptions.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(NoResourceFoundException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                "The requested Resource could not be found: " + e.getResourcePath()
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(
            HandlerMethodValidationException ex) {

        log.warn("Handler method validation error: {}", ex.getMessage());

        List<String> errors = ex.getValueResults()
                .stream()
                .map(result -> {
                    String parameterName = result.getMethodParameter().getParameterName();
                    String defaultMessage = result.getResolvableErrors().isEmpty()
                            ? "Invalid value"
                            : result.getResolvableErrors().get(0).getDefaultMessage();
                    return String.format("%s %s", parameterName, defaultMessage);
                })
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Request parameter validation failed",
                errors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParamException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        String message = "Missing parameter";
        List<String> errors = List.of("Missing parameter: " + e.getParameterName());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                message,
                errors
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParamException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = "Invalid Request";
        List<String> errors = e.getFieldErrors().stream().map(
                fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage()
        ).toList();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                message,
                errors
        );
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(MovieNotFoundException e) {
        return new ErrorResponse(
                "Not Found",
                e.getMessage()
        );
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnAuthorizedException(UnAuthorizedException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.name(),
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Something went wrong, Please try again"
        );
    }
}
