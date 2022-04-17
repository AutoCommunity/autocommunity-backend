package com.autocommunity.backend.web;


import com.autocommunity.backend.Dto.UserDto;
import com.autocommunity.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequestMapping(path = "/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService;

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
