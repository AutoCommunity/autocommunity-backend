package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.SessionEntity;
import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.exception.UnauthenticatedException;
import com.autocommunity.backend.repository.SessionRepository;
import com.autocommunity.backend.web.AbstractController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    private final long sessionDefaultLifetime = TimeUnit.DAYS.toMillis(1);


    @Transactional(rollbackFor = Throwable.class)
    public SessionEntity createSession(UserEntity user, String session) {
        var sessionEntity = SessionEntity.builder()
            .session(session)
            .status(SessionEntity.Status.ACTIVE)
            .creationTime(new Date())
            .expirationTime(new Date(new Date().getTime() + sessionDefaultLifetime))
            .user(user)
            .build();
        sessionRepository.save(sessionEntity);
        return sessionEntity;
    }

    @Transactional(readOnly = true)
    public SessionEntity getSession(String session) {
        var sessionEntity = sessionRepository.findBySession(session);
        if (sessionEntity.isPresent() && sessionEntity.get().getExpirationTime().after(new Date())) {
            return sessionEntity.get();
        } else {
            return null;
        }
    }

    public Mono<Void> isUserAuthorised(ServerWebExchange webExchange) {
        if (webExchange.getRequest().getCookies().get("SESSION") == null ||
            webExchange.getRequest().getCookies().get("SESSION").size() < 1 ||
            this.getSession(webExchange.getRequest().getCookies().get("SESSION").get(0).getValue()) == null) {
            return Mono.error(new UnauthenticatedException());
        }
        return Mono.empty();
    }

}
