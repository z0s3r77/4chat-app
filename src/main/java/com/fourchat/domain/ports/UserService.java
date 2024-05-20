package com.fourchat.domain.ports;

import com.fourchat.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createBasicUser(String userName, String email, String firstName, String lastName);

    Optional<User> getUserByUserName(String userName);

    User getUserById(String userId);

    List<User> autocompleteUsersByName(String name);

    boolean deleteUserFromContacts(String userId, String userIdToDelete);

    User save(User user);
}
