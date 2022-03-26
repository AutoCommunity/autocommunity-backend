package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.exception.ValidationException;
import com.autocommunity.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Mono<UserEntity> registerUser(
        String email,
        String username,
        String password,
        String password2
    ) {
        if (!password.equals(password2)) return Mono.error(
            new ValidationException("passwords don't match")
        );

        return Mono.defer(() -> Mono.just(
            userRepository.findByEmailOrUsername(email, username)
                .filter(user -> {
                    throw new AlreadyExistsException("User with that email or username already exists");
                })

        )).map(p -> {
            var userEntity = UserEntity.builder()
                .email(email)
                .username(username)
                //TODO: encode password
                .passwordHash(password)
                .build();
            userRepository.save(userEntity);
            return userEntity;
        });

    }
}
