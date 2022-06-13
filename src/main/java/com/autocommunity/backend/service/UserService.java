package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.user.SessionEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.exception.IncorrectPasswordException;
import com.autocommunity.backend.exception.NotFoundException;
import com.autocommunity.backend.repository.UserRepository;
import com.autocommunity.backend.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;

    @Transactional(readOnly = true)
    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(rollbackFor = Throwable.class)
    public SessionEntity registerUser(
        String username,
        String password
    ) {
        var sessionId = UUIDUtils.randomBase64UUID();

        userRepository.findByUsername(username).ifPresent(notUsed -> {
            throw new AlreadyExistsException("User with that email or username already exists");
        });

        var userEntity = UserEntity.builder()
            .username(username)
            //TODO: encode password
            .passwordHash(password)
            .build();
        userRepository.save(userEntity);
        var session = sessionService.createSession(userEntity, sessionId);
        session.setFirstRegistration(Boolean.TRUE);
        log.info("registered user with id: {}, username: {}", userEntity.getId(), userEntity.getUsername());
        return session;
    }

    @Transactional(rollbackFor = Throwable.class)
    public SessionEntity loginUser(
        String username,
        String password
    ) {
        var sessionId = UUIDUtils.randomBase64UUID();

        var userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found."));
        if (!userEntity.getPasswordHash().equals(password))
            throw new IncorrectPasswordException("Incorrect password.");

        var session = sessionService.createSession(userEntity, sessionId);
        session.setFirstRegistration(Boolean.FALSE);
        return session;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void logoutUser(UserEntity user) {
        sessionService.getActiveSessions(user)
            .forEach(sessionService::terminateSession);
    }

}