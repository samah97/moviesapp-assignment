package com.backbase.moviesapp.exceptions;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
