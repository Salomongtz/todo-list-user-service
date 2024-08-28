package com.mindhub.user_service.services.implementations;

import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.NewUserRecord;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.repositories.UserRepository;
import com.mindhub.user_service.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;


@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Mono<UserEntity> getUserById(Long id) {
        if (isInvalidId(id)) {
            return Mono.error(new IllegalArgumentException("Invalid user ID, must be greater than 0"));
        }
        return userRepository.findById(id);
    }

    @Override
    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserEntity> createUser(NewUserRecord newUser) {
        if (isInvalidUserRecord(newUser)) {
            return Mono.error(new IllegalArgumentException("Invalid user data, all fields are required"));
        }
        UserEntity user = new UserEntity(newUser.name(), newUser.email(), newUser.password());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user)
                .onErrorResume(e -> Mono.error(new DataIntegrityViolationException("Email already in use")));
    }


    @Override
    public Mono<UserEntity> updateUser(Long id, NewUserRecord newUser) {
        if (isInvalidId(id)) {
            return Mono.error(new IllegalArgumentException("Invalid user ID, must be greater than 0"));
        }
        if (isInvalidUserRecord(newUser)) {
            return Mono.error(new IllegalArgumentException("Invalid user data, all fields are required"));
        }

        return userRepository.findById(id).flatMap(existingUser -> {
            existingUser.setName(newUser.name());
            existingUser.setEmail(newUser.email());
            existingUser.setPassword(bCryptPasswordEncoder.encode(newUser.password()));
            return userRepository.save(existingUser)
                    .onErrorResume(e -> Mono.error(new DataIntegrityViolationException("Email already in use")));
        }).switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        if (isInvalidId(id)) {
            return Mono.error(new IllegalArgumentException("Invalid user ID, must be greater than 0"));
        }
        return userRepository.findById(id).flatMap(userRepository::delete)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    private boolean isInvalidUserRecord(NewUserRecord newUser) {
        return newUser == null || newUser.name().isEmpty() || newUser.email().isEmpty();
    }

    private boolean isInvalidId(Long id) {
        return id == null || id <= 0;
    }
}
