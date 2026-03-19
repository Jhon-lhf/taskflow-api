package com.jhonatan.taskflow.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("User Already exists with email: " + email);
    }
}
