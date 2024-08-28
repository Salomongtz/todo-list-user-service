package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class UserDTO {

    private final Long id;
    private final String name;
    private final String email;
    @Getter(AccessLevel.NONE)
    private final String password;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
    }
}
