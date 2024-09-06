package com.mindhub.user_service.services;

import com.mindhub.user_service.models.NewUserRecord;
import com.mindhub.user_service.models.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserService {
    Mono<UserEntity> getUserById(Long id);

    Flux<UserEntity> getAllUsers();


    Mono<UserEntity> createUser(NewUserRecord newUser);

    Mono<UserEntity> updateUser(Long id, NewUserRecord newUser);

    Mono<Void> deleteUser(Long id);

    Mono<UserEntity> getUserByEmail(String username);
}
