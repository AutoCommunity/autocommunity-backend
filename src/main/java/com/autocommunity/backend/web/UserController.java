package com.autocommunity.backend.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping(path = "/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @PostMapping("/register")
    public Mono<String> registerUser() {
        return Mono.just("Hello");
    }
}
