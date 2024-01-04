package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.controller.beans.UserRequest;
import com.hodbenor.project.eventsservice.security.TokenService;
import com.hodbenor.project.eventsservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequest userRequest) {
        AtomicReference<String> loginToken = new AtomicReference<>("");
        userService.signUp(userRequest.getUsername(), userRequest.getPassword())
                .ifPresent(user -> {
                    loginToken.set(user.getLoginToken());
                });

        return loginToken.get().length() > 0 ? ResponseEntity.ok(loginToken.get()) : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/valid")
    public ResponseEntity<Boolean> valid(@RequestHeader("Authorization") String token) {
        boolean isValid = false;
        if (userService.findUserByToken(token).isPresent()) {
                isValid = tokenService.validToken(token);
        }

        return isValid ? ResponseEntity.ok(true) : ResponseEntity.internalServerError().build();
    }

   /* @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.username(), userRequest.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Logged in successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }*/
}
