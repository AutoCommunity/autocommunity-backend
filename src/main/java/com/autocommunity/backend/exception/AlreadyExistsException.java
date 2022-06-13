package com.autocommunity.backend.exception;

public class AlreadyExistsException extends BadRequestException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
