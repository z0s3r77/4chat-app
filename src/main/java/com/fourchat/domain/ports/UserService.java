package com.fourchat.domain.ports;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;

import java.util.Optional;

public interface UserService {

    BasicUser createBasicUser(String name, String email);
    Optional<User> getUserByUserName(String userName);
    BasicUser getUserById(String userId);
}
