package com.autocommunity.backend.exception;

public class IncorrectPasswordException extends BadRequestException {
    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
