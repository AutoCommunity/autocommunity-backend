package com.autocommunity.backend.web;


import com.autocommunity.backend.Dto.UserDto;
import com.autocommunity.backend.exception.AlreadyExistsException;
import com.autocommunity.backend.service.UserService;
import com.autocommunity.backend.util.AuthContext;
import com.autocommunity.backend.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
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
        @RequestBody @Valid UserDto user,
        ServerWebExchange webExchange) {
        return userService.registerUser(user);
    }

    /*@PostMapping("/login")
    public Mono<ReplyBase> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password, ServerWebExchange webExchange) {
        //TODO: user login
        return Mono.empty();
    }*/

}
