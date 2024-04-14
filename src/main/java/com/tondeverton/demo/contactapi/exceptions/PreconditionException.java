package com.tondeverton.demo.contactapi.exceptions;

public class PreconditionException extends RuntimeException {
    public PreconditionException(String message) {
        super(message);
    }
}
