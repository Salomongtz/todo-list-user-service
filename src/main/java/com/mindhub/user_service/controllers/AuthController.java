package com.mindhub.user_service.controllers;

import com.mindhub.user_service.configuration.JwtUtil;
import com.mindhub.user_service.models.NewUserRecord;
import com.mindhub.user_service.models.UserLogin;
import com.mindhub.user_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private ReactiveAuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user was created and returned."),
            @ApiResponse(responseCode = "400", description = "The user data provided is invalid."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<?> createUser(
            @Parameter(
                    name = "user",
                    description = "The user to create.",
                    example = """
                            {
                              "name": "John Doe",
                              "email": "john.doe@example.com",
                              "password": "password123"
                            }
                            """,
                    required = true)
            @RequestBody
            NewUserRecord user) {
        return userService.createUser(user).map(currentUser -> ResponseEntity.ok(user.name() + " was created."))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user was logged in and returned."),
            @ApiResponse(responseCode = "400", description = "The user data provided is invalid."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<String>> loginUser(
            @Parameter(
                    name = "user",
                    description = "The user to login.",
                    example = """
                            {
                              "email": "john.doe@example.com",
                              "password": "password123"
                            }
                            """,
                    required = true)
            @RequestBody
            UserLogin user) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.email(), user.password())
                ).map(authentication ->
                {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String jwt = jwtUtil.generateToken(userDetails.getUsername());
                    return ResponseEntity.ok(jwt);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())));
    }
}
