package com.autocommunity.backend.util;

import com.autocommunity.backend.entity.user.SessionEntity;
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
    private final Boolean httpsEnabled;

    private static final String SESSION_COOKIE_NAME = "SESSION";
    public void attach(ServerWebExchange webExchange, SessionEntity session) {
        var cookie = ResponseCookie.from(SESSION_COOKIE_NAME, session.getSession())
            .maxAge(Duration.ofMillis(session.getExpirationTime().getTime() - session.getCreationTime().getTime()).toSeconds())
            .path("/")
            .secure(false)
            .httpOnly(true);
        if (httpsEnabled)
            cookie = cookie.sameSite("None").secure(true);
        webExchange.getResponse().addCookie(
            cookie.build()
        );
    }

    public Mono<SessionEntity> isUserAuthorised(ServerWebExchange webExchange) {
        return Optional.ofNullable(
            webExchange.getRequest().getCookies().get(SESSION_COOKIE_NAME)
        ).filter(
            httpCookies -> !httpCookies.isEmpty()
        ).map(
            httpCookies -> Mono.just(sessionService.getSession(httpCookies.get(0).getValue()))
        ).orElse(Mono.error(new UnauthenticatedException()));
    }

}