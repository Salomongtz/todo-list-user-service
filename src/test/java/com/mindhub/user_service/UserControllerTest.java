package com.mindhub.user_service;

import com.mindhub.user_service.controllers.UserController;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.models.NewUserRecord;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private UserController userController;

    @BeforeEach
public void setUp() {
    MockitoAnnotations.openMocks(this); // Use openMocks instead of initMocks
    userController = new UserController(userService);
}

    @Test
    public void testGetUserById_ValidId_200ResponseAndCorrectUserData() {
        // Given
        Long userId = 1L;
        NewUserRecord userRecord = new NewUserRecord("John Doe", "john.doe@example.com", "password123");
        UserEntity userEntity = new UserEntity(userRecord.name(),userRecord.email(), userRecord.password());
        UserDTO expectedUserDto = new UserDTO(userEntity);

        when(userService.getUserById(userId)).thenReturn(Mono.just(userEntity));

        // When
        Mono<ResponseEntity<UserDTO>> actualUserDtoMono = userController.getUserById(userId);

        // Then
        UserDTO actualUserDto = Objects.requireNonNull(actualUserDtoMono.block()).getBody();
        assert actualUserDto != null;
        assertEquals(expectedUserDto.getId(), actualUserDto.getId());
    }

//    @Test
//    public void testGetUserById_InvalidId_404Response() {
//        // Given
//        Long userId = 1L;
//
//        when(userService.getUserById(userId)).thenReturn(Mono.empty());
//
//        // When
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
//                userController.getUserById(userId).block());
//
//        // Then
//        assertEquals(userId, exception.getUserId());
//    }
}