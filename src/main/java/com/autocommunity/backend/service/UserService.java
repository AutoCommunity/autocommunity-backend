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
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionService sessionService;


    public Mono<SessionEntity> registerUser(
        String username,
        String password
    ) {
        var sessionId = RandomUtils.randomBase64UUID();

        return Mono.defer(() -> Mono.just(
            userRepository.findByUsername(username)
                .filter(user -> {
                    throw new AlreadyExistsException("User with that email or username already exists");
                })

        )).map(p -> {
            var userEntity = UserEntity.builder()
                .username(username)
                //TODO: encode password
                .passwordHash(password)
                .build();
            userRepository.save(userEntity);
            var session = sessionService.createSession(userEntity, sessionId);
            session.setFirstRegistration(Boolean.TRUE);
            return session;
        });
    }

    public Mono<SessionEntity> loginUser(
        String username,
        String password
    ) {
        var sessionId = RandomUtils.randomBase64UUID();

        return Mono.just(userRepository.findByUsername(username))
            .flatMap(optionalUserEntity -> {
                if (optionalUserEntity.isEmpty()) {
                    return Mono.error(new UserNotFoundException("User not found."));
                }
                return Mono.just(optionalUserEntity.get());
            })
            .flatMap(userEntity -> {
                if (userEntity.getPasswordHash().equals(password))
                    return Mono.just(userEntity);
                return Mono.error(new IncorrectPasswordException("Incorrect password."));
            })
            .map(userEntity -> sessionService.createSession(userEntity, sessionId))
            .map(sessionEntity -> {
                sessionEntity.setFirstRegistration(Boolean.FALSE);
                return sessionEntity;
            });
    }
}