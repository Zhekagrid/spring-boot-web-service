package com.example.webservice.exception;

public class UserNonAuthenticatedException extends Exception {
    public UserNonAuthenticatedException() {
    }

    public UserNonAuthenticatedException(String message) {
        super(message);
    }

    public UserNonAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNonAuthenticatedException(Throwable cause) {
        super(cause);
    }
}
