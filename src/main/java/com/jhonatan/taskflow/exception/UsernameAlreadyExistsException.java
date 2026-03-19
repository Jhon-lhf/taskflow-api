package com.jhonatan.taskflow.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists with username: " + username);
    }
}
