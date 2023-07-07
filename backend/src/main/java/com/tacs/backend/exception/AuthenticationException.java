package com.tacs.backend.exception;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String msg) {
        super(msg);
    }
}
