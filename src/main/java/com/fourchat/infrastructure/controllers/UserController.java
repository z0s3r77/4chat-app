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
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_admin_client_role','ROLE_user_client_role')")
@RequestMapping(ApiConstants.USERS_URL)
public class UserController {

    UserService userService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") String userId) {
        return userService.getUserById(userId);
    }


    @GetMapping("/autocomplete")
    public List<User> autocompleteUsersByName(@RequestParam("name") String name, Authentication authentication) {

        if (name.length() < 3) {
            throw new RuntimeException("Name must have at least 3 characters");
        }

        return userService.autocompleteUsersByName(name);
    }

    @GetMapping("/user/get-contacts")
    public List<User> getContacts(Authentication authentication) {

        var jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();

        User user = userService.createBasicUser(authentication.getName(), claims.get("email").toString(), claims.get("given_name").toString(), claims.get("family_name").toString());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<User> contacts = user.getContacts().stream().map(contactId -> userService.getUserById(contactId)).toList();

        return contacts;
    }


    @PostMapping("/user/addContact")
    public ResponseEntity<User> addContact(@RequestParam("contactId") String contactId, Authentication authentication) {

        var jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();

        User user = userService.createBasicUser(authentication.getName(), claims.get("email").toString(), claims.get("given_name").toString(), claims.get("family_name").toString());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.addContact(contactId);
        userService.save(user);

        return ResponseEntity.ok(user);
    }


    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(Authentication authentication) {

        var jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();

        User user = userService.createBasicUser(authentication.getName(), claims.get("email").toString(), claims.get("given_name").toString(), claims.get("family_name").toString());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserDto userDto = userDtoMapper.toUserDto(user, claims);


        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveUser(@RequestBody UserDto userDto) {

        User user = userService.getUserById(userDto.getId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        User updatedUser = userDtoMapper.toUser(userDto);
        updatedUser.setContacts(user.getContacts());

        return ResponseEntity.ok(userService.save(updatedUser));
    }

}
