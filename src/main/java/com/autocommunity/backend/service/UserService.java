package com.autocommunity.backend.service;

import com.autocommunity.backend.Dto.UserDto;
import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.repository.UserRepository;
import com.autocommunity.backend.web.AbstractController;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Mono<AbstractController.ReplyBase> registerUser(
        UserDto user
    ) {

        return Mono.defer(() -> Mono.just(
            userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername())
                .filter(foundUser -> {
                    throw new AlreadyExistsException("User with that email or username already exists");
                })

        )).map(p -> {
            var userEntity = UserEntity.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                //TODO: encode password
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .build();
            userRepository.save(userEntity);
            return AbstractController.ReplyBase.success("user registered");
        });

    }
}
