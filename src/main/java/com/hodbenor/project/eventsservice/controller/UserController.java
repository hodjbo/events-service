package com.hodbenor.project.eventsservice.controller;

import com.hodbenor.project.eventsservice.controller.beans.UserRequest;
import com.hodbenor.project.eventsservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@RequestBody UserRequest userRequest) {
        long userId = userService.signUp(userRequest.getUsername(), userRequest.getPassword());

        return userId > 0 ? ResponseEntity.ok(userId) : ResponseEntity.internalServerError().build();
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
