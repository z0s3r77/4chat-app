package com.fourchat.infrastructure.controllers.mappers;


import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.infrastructure.controllers.dtos.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class UserDtoMapper {

    public UserDto toUserDto(User user, Map<String, Object> claims) {

        return UserDto.builder()
                .id(user.getId())
                .displayName(user.getUserName())
                .firstName(claims.get("given_name").toString())
                .lastName(claims.get("family_name").toString())
                .photoUrl(user.getPhotoUrl())
                .email(user.getEmail())
                .description(user.getDescription())
                .twitter(user.getTwitter())
                .linkedIn(user.getLinkedIn())
                .facebook(user.getFacebook())
                .contacts(null)
                .build();
    }

    public User toUser(UserDto userDto) {

        return BasicUser.builder()
                .id(userDto.getId())
                .userName(userDto.getDisplayName())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .description(userDto.getDescription())
                .contacts(null)
                .linkedIn(userDto.getLinkedIn())
                .twitter(userDto.getTwitter())
                .facebook(userDto.getFacebook())
                .photoUrl(userDto.getPhotoUrl())
                .build();
    }


}
