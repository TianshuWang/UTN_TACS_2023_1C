package com.tacs.backend.exception;

public class UserIsNotOwnerException extends RuntimeException{
    public UserIsNotOwnerException(String msg) {
        super(msg);
    }
}
