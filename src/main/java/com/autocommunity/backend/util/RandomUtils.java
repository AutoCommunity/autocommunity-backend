package com.autocommunity.backend.util;

import java.util.Base64;
import java.util.UUID;

public class RandomUtils {
    public static String randomBase64UUID() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }
}
