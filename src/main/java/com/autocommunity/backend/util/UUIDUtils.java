package com.autocommunity.backend.util;

import com.autocommunity.backend.exception.InvalidIdentifierException;

import java.util.Base64;
import java.util.UUID;

public class UUIDUtils {
    public static String randomBase64UUID() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    public static UUID parseUUID(String rawUUID) {
        try {
            return UUID.fromString(rawUUID);
        } catch (IllegalArgumentException e) {
            throw new InvalidIdentifierException();
        }
    }

}
