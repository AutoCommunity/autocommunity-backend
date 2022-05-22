package com.autocommunity.backend.web;


import com.autocommunity.backend.exception.IncorrectPasswordException;
import com.autocommunity.backend.exception.UserNotFoundException;
import com.autocommunity.backend.service.UserService;
import com.autocommunity.backend.util.AuthContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RequestMapping(path = "/api/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final AuthContext authContext;


    @PostMapping("/auth")
    public Mono<ReplyBase> auth(@RequestBody @Valid AuthRequest request, ServerWebExchange webExchange) {
        return userService.loginUser(request.getLogin(), request.getPassword())
            .onErrorResume(
                UserNotFoundException.class,
                notUsed -> userService.registerUser(request.getLogin(), request.getPassword())
            )
            .map(sessionEntity -> {
                authContext.attach(webExchange, sessionEntity);
                return sessionEntity.getFirstRegistration() ?
                    ReplyBase.success("Successfully registered.") :
                    ReplyBase.success("Successfully logged in.");
            })
            .onErrorResume(
                IncorrectPasswordException.class,
                e -> Mono.just(ReplyBase.failure(e.getMessage()))
            );
    }


    @Getter
    @RequiredArgsConstructor
    public static class AuthRequest {
        @NotBlank
        private final String login;
        @NotBlank
        private final String password;
    }

}
