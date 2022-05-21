package com.autocommunity.backend.util;

import com.autocommunity.backend.entity.SessionEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;

@Component
public class AuthContext {
    public void attach(ServerWebExchange webExchange, SessionEntity session) {
        webExchange.getResponse().addCookie(
                ResponseCookie.from("SESSION", session.getSession())
                        .maxAge(Duration.ofMillis(session.getExpirationTime().getTime() - session.getCreationTime().getTime()).toSeconds())
                        .path("/")
                        .secure(false)
                        .httpOnly(true)
                        .build()
        );
    }
}