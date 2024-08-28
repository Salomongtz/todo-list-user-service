package com.mindhub.user_service.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("users")
public class UserEntity {
    // Getters and setters
    @Id
    private Long id;
    private String name;
    private String email;
    @ToStringExclude
    private String password;

    // Constructor
    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Empty constructor for Spring Data
    public UserEntity() {
    }


}
