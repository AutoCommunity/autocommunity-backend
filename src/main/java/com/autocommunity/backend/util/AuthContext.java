package com.autocommunity.backend.util;

import com.autocommunity.backend.entity.SessionEntity;
import com.autocommunity.backend.exception.UnauthenticatedException;
import com.autocommunity.backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthContext {
    private final SessionService sessionService;

    private static final String sessionCookieName = "SESSION";
    public void attach(ServerWebExchange webExchange, SessionEntity session) {
        webExchange.getResponse().addCookie(
                ResponseCookie.from(sessionCookieName, session.getSession())
                        .maxAge(Duration.ofMillis(session.getExpirationTime().getTime() - session.getCreationTime().getTime()).toSeconds())
                        .path("/")
                        .secure(false)
                        .httpOnly(true)
                        .build()
        );
    }

    public Mono<Void> isUserAuthorised(ServerWebExchange webExchange) {
        return Optional.ofNullable(
            webExchange.getRequest().getCookies().get(sessionCookieName)
        ).filter(
            httpCookies -> !httpCookies.isEmpty()
        ).filter(
            httpCookies -> sessionService.getSession(httpCookies.get(0).getValue()) != null
        )
            .map(httpCookies -> (Mono<Void>)((Object)Mono.empty()))
            .orElse(Mono.error(new UnauthenticatedException()));
    }

}