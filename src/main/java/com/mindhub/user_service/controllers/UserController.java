package com.mindhub.user_service.controllers;

import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.NewUserRecord;
import com.mindhub.user_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Gets the user with the specified id from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user was found and returned."),
            @ApiResponse(responseCode = "404", description = "The user was not found."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable(name = "id")
                                                     @Parameter(description = "The id of the user to get.", required
                                                             = true) Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(new UserDTO(user)))
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    @GetMapping
    @Operation(summary = "Get all users",
            description = "Returns a list of all users present in the database. The list is not paginated, so be " +
                    "careful when using this endpoint with large datasets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The users were found and returned."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<Flux<UserDTO>>> getAllUsers() {
        return Mono.just(ResponseEntity.ok(userService.getAllUsers().map(UserDTO::new)));
    }

    @PostMapping
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user was created and returned."),
            @ApiResponse(responseCode = "400", description = "The user data provided is invalid."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<String>> createUser(
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
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user was updated and returned."),
            @ApiResponse(responseCode = "400", description = "The user data provided is invalid."),
            @ApiResponse(responseCode = "404", description = "The user was not found."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<String>> updateUser(
            @PathVariable(name = "id")
            @Parameter(description = "The id of the user to get.",
                    required = true) Long id,
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
            @RequestBody NewUserRecord user) {
        return userService.updateUser(id, user)
                .map(currentUser -> ResponseEntity.ok(user.name() + " was updated."))
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The user was deleted."),
            @ApiResponse(responseCode = "404", description = "The user was not found."),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred.")
    })
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.ok("User with id " + id + " was deleted.")))
                .onErrorResume(e -> Mono.error(new UserNotFoundException(id)));
    }
}