package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.user.SessionEntity;
import com.autocommunity.backend.exception.UserNotFoundException;
import com.autocommunity.backend.service.SessionService;
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
import javax.validation.constraints.Size;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
@RequestMapping(path = "/api/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;
    private final AuthContext authContext;
    private final SessionService sessionService;


    @PostMapping("/auth")
    public Mono<ReplyBase> auth(@RequestBody @Valid AuthRequest request, ServerWebExchange webExchange) {
        return
            Mono.just(loginOrRegister(request.getUsername(), request.getPassword()))
            .doOnNext(session -> authContext.attach(webExchange, session))
            .flatMap(session -> Mono.just(
                session.getFirstRegistration() ?
                    ReplyBase.success("Successfully registered.") :
                    ReplyBase.success("Successfully logged in.")
            ));
    }

    @PostMapping("/logout")
    public Mono<ReplyBase> logout(ServerWebExchange webExchange) {
        return
            authContext.isUserAuthorised(webExchange)
                .doOnNext(session -> userService.logoutUser(session.getUser()))
                .doOnNext(session -> {
                    session.setExpirationTime(session.getCreationTime()); // set ttl to 0
                    authContext.attach(webExchange, session);
                })
                .then(Mono.just(ReplyBase.success("Successfully logged out.")));
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
        @Size(min = 1)
        private final String username;
        @NotBlank
        private final String password;
    }

}
