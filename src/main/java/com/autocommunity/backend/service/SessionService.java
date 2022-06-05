package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.user.SessionEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import com.autocommunity.backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
        return sessionRepository.findBySession(session)
            .filter(sessionEntity -> sessionEntity.getStatus() == SessionEntity.Status.ACTIVE)
            .filter(sessionEntity -> !sessionEntity.isExpired())
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SessionEntity> getActiveSessions(UserEntity user) {
        return sessionRepository.findAllByUserAndStatus(user, SessionEntity.Status.ACTIVE);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void terminateSession(SessionEntity session) {
        if (session.getStatus() == SessionEntity.Status.EXPIRED)
            return;
        session.setStatus(SessionEntity.Status.EXPIRED);
        session.setExpirationTime(new Date());
        sessionRepository.save(session);
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional(rollbackFor = Throwable.class)
    public void expireSessions() {
        sessionRepository.findAllByExpirationTimeLessThanEqualAndStatus(new Date(), SessionEntity.Status.ACTIVE)
            .forEach(this::terminateSession);
    }

}
