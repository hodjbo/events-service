package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.controller.beans.UserRequest;
import com.hodbenor.project.eventsservice.security.TokenService;
import com.hodbenor.project.eventsservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest userRequest) {

        return Optional.ofNullable(userService.login(userRequest.getUsername(), userRequest.getPassword()))
                .map(ResponseEntity::ok).orElse(new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        AtomicReference<String> loginToken = new AtomicReference<>("");
        userService.signUp(userRequest.getUsername(), userRequest.getPassword())
                .ifPresent(user -> loginToken.set(user.getAuthToken()));

        return loginToken.get().length() > 0 ? ResponseEntity.ok(loginToken.get()) : ResponseEntity.internalServerError().build();
    }
}
