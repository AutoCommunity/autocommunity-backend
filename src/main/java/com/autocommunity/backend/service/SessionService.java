package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.SessionEntity;
import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    private final long SESSION_DEFAULT_LIFETIME = TimeUnit.DAYS.toMillis(1);


    public SessionEntity createSession(UserEntity user, String session) {
        var sessionEntity = SessionEntity.builder()
            .session(session)
            .status(SessionEntity.Status.ACTIVE)
            .creationTime(new Date())
            .expirationTime(new Date(new Date().getTime() + SESSION_DEFAULT_LIFETIME))
            .user(user.getId())
            .build();
        sessionRepository.save(sessionEntity);
        return sessionEntity;
    }
}
