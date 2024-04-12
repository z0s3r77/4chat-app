package com.fourchat.domain.ports;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;

import java.util.Optional;

public interface UserService {

    User createBasicUser(String name, String email);
    Optional<User> getUserByUserName(String userName);
    User getUserById(String userId);
}
