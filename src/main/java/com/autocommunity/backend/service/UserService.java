package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.SessionEntity;
import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.exception.IncorrectPasswordException;
import com.autocommunity.backend.exception.UserNotFoundException;
import com.autocommunity.backend.repository.UserRepository;
import com.autocommunity.backend.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;


    @Transactional(rollbackFor = Throwable.class)
    public SessionEntity registerUser(
        String username,
        String password
    ) {
        var sessionId = RandomUtils.randomBase64UUID();

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
        return session;
    }

    @Transactional(rollbackFor = Throwable.class)
    public SessionEntity loginUser(
        String username,
        String password
    ) {
        var sessionId = RandomUtils.randomBase64UUID();

        var userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!userEntity.getPasswordHash().equals(password))
            throw new IncorrectPasswordException("Incorrect password.");

        var session = sessionService.createSession(userEntity, sessionId);
        session.setFirstRegistration(Boolean.FALSE);
        return session;
    }
}