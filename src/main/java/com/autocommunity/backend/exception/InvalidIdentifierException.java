package com.autocommunity.backend.exception;

public class InvalidIdentifierException extends BadRequestException {
    public InvalidIdentifierException() {
        super("Invalid identifier.");
    }
}
