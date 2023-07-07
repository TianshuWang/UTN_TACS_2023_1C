package com.tacs.backend.exception;

public class RequestNotAllowException extends RuntimeException {
    public RequestNotAllowException(String msg) {
        super(msg);
    }
}
