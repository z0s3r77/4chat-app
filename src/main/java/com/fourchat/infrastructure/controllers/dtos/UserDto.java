package com.fourchat.infrastructure.controllers.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {

    private String id;
    private String displayName;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String email;
    private String description;
    private List<UserDto> contacts;

}
