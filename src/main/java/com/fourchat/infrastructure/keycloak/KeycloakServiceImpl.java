package com.fourchat.infrastructure.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {


    /**
     * This method returns all users from the Keycloak realm
     *
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource().users().list();
    }

    /**
     * This method searches for a user by username
     *
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    /**
     * This method creates a user in the Keycloak realm
     *
     * @return String
     */
    @Override
    public String createUser(@NonNull UserDTO userDTO) {

        int status;
        UsersResource userResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation);
        status = response.getStatus();

        if (status == 201) {

            log.info("User created successfully");
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.password());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();


            List<RoleRepresentation> rolesRepresentation;

            if (userDTO.roles() == null || userDTO.roles().isEmpty()) {

                rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
            } else {

                rolesRepresentation = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.roles().stream().anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(rolesRepresentation);


            return "User created successfully";
        } else if (status == 409) {

            log.error("User already exists");
            return "User already exists";

        } else {
            log.error("Error creating user, please contact with the administrator");
            return "Error creating user, please contact with the administrator";
        }
    }

    /**
     * This method deletes a user from the Keycloak realm
     */
    @Override
    public void deleteUser(String userId) {

        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }


    /**
     * This method updates a user in the Keycloak realm
     */
    @Override
    public void updateUser(String userId, @NonNull UserDTO userDTO) {

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.password());

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);

        userResource.update(userRepresentation);

    }
}
