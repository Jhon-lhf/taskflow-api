package com.jhonatan.taskflow.exception;

public class UnauthorizedProjectActionException extends RuntimeException {
    public UnauthorizedProjectActionException(String message) {
        super(message);
    }
}
