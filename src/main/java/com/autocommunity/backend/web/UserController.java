package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.SessionEntity;
import com.autocommunity.backend.exception.UserNotFoundException;
import com.autocommunity.backend.service.UserService;
import com.autocommunity.backend.util.AuthContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@RequestMapping(path = "/api/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final AuthContext authContext;


    @PostMapping("/auth")
    public Mono<ReplyBase> auth(@RequestBody @Valid AuthRequest request, ServerWebExchange webExchange) {
        try {
            var session = loginOrRegister(request.getUsername(), request.getPassword());
            authContext.attach(webExchange, session);
            return Mono.just(
                session.getFirstRegistration() ?
                    ReplyBase.success("Successfully logged in.") :
                    ReplyBase.success("Successfully registered.")
            );
        } catch (RuntimeException e) {
            return Mono.just(ReplyBase.failure(e.getMessage()));
        }
    }

    private SessionEntity loginOrRegister(String login, String password) {
        try {
            return userService.loginUser(login, password);
        } catch (UserNotFoundException e) {
            return userService.registerUser(login, password);
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class AuthRequest {
        @NotBlank
        private final String username;
        @NotBlank
        private final String password;
    }

}
