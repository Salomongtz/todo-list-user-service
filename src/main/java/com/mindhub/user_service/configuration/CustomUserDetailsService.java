package com.mindhub.user_service.configuration;

import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.getUserByEmail(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException(username)))
                .map(user -> new User(user.getEmail(), user.getPassword(), new ArrayList<>()));
    }
}
