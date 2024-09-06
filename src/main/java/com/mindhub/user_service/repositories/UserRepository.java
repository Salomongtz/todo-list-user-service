package com.mindhub.user_service.repositories;

import com.mindhub.user_service.models.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<Boolean> existsByEmail(String email);

    Mono<UserEntity> findByEmail(String username);
}
