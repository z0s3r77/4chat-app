package com.fourchat.infrastructure.controllers;


import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.dtos.UserDto;
import com.fourchat.infrastructure.controllers.mappers.UserDtoMapper;
import com.fourchat.infrastructure.controllers.util.ApiConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_admin_client_role')")
@RequestMapping(ApiConstants.USERS_URL)
public class UserController {

    UserService userService;
    private final UserDtoMapper userDtoMapper;


    @GetMapping("/autocomplete")
    public List<User> autocompleteUsersByName(@RequestParam("name") String name, Authentication authentication) {

        if (name.length() < 3) {
            throw new RuntimeException("Name must have at least 3 characters");
        }

        return userService.autocompleteUsersByName(name);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {

        var jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();

        User user = userService.createBasicUser(authentication.getName(), claims.get("email").toString());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserDto userDto = userDtoMapper.toUserDto(user, claims);


        return ResponseEntity.ok(userDto);
    }


}
