package com.fourchat.infrastructure.controllers;


import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.controllers.util.ApiConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_admin_client_role')")
@RequestMapping(ApiConstants.USERS_URL)
public class UserController {

    UserService userService;


    @GetMapping("/autocomplete")
    public List<User> autocompleteUsersByName(@RequestParam("name") String name, Authentication authentication) {

        if (name.length() < 3) {
            throw new RuntimeException("Name must have at least 3 characters");
        }

        return userService.autocompleteUsersByName(name);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(Authentication authentication) {

        var jwt = (Jwt) authentication.getPrincipal();
        var email = (String) jwt.getClaims().get("email");

        return ResponseEntity.ok(userService.createBasicUser(authentication.getName(), email));
    }


}
