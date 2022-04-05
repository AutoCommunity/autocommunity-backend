package com.autocommunity.backend.security;

import com.autocommunity.backend.entity.UserEntity;
import com.autocommunity.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class MyUserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Optional<UserEntity> foundUser = userRepository.findByUsername(username);
        return foundUser.map(user -> Mono.just(user.toUserDetails())).
                orElseGet(() -> Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))));
    }
}
