package com.fourchat.infrastructure.controllers;


import com.fourchat.infrastructure.keycloak.IKeycloakService;
import com.fourchat.infrastructure.keycloak.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/keycloak/user")
@AllArgsConstructor
public class KeycloakController {


    private IKeycloakService keycloakService;

    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers() {

        return ResponseEntity.ok(keycloakService.findAllUsers());
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username) {

        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {

        String response = keycloakService.createUser(userDTO);
        return ResponseEntity.created(new URI("/keycloak/user/create/")).body(response);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO) {

        keycloakService.updateUser(userId, userDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('admin_client_role')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {

        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


}
