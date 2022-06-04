package com.autocommunity.backend.exception;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("Not authenticated");
    }
}
