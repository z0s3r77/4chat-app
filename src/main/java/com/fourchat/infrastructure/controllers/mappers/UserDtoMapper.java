package com.fourchat.infrastructure.controllers.mappers;


import com.fourchat.domain.models.User;
import com.fourchat.infrastructure.controllers.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class UserDtoMapper {

    public UserDto toUserDto(User user, Map<String, Object> claims) {

        return UserDto.builder()
                .id(user.getId())
                .displayName(user.getUserName())
                .firstName( claims.get("given_name").toString())
                .lastName( claims.get("family_name").toString())
                .photoUrl(null)
                .email(user.getEmail())
                .build();
    }



}
