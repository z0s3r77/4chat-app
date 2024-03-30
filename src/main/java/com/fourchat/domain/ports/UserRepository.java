package com.fourchat.domain.ports;

import com.fourchat.domain.models.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findUserByUserName(String userName);
    User save(User user);
    Optional<User> findById(String userId);

}
