package com.autocommunity.backend.web;


import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.service.UserService;
import com.autocommunity.backend.util.AuthContext;
import com.autocommunity.backend.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.UUID;

@RequestMapping(path = "/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final AuthContext authContext;

    @PostMapping("/register")
    public Mono<ReplyBase> register(
        @RequestParam("email") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("password2") String password2,
        ServerWebExchange webExchange) {
        return userService.registerUser(email, username, password, password2)
            .map(sessionEntity -> {
                authContext.attach(webExchange, sessionEntity);
                return ReplyBase.success("user registered");
            });
    }

    @PostMapping("/login")
    public Mono<ReplyBase> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password, ServerWebExchange webExchange) {
        //TODO: user login
        return Mono.empty();
    }

}
