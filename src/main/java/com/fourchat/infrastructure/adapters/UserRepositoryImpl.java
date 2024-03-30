package com.fourchat.infrastructure.adapters;

import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger();


    @Override
    public Optional<User> findUserByUserName(String userName) {
        return this.users.stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst();
    }

    @Override
    public User save(User user) {

        Optional<User> existingUser = this.findUserByUserName(user.getUserName());

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            user.setId(String.valueOf(this.idCounter.incrementAndGet()));
            this.users.add(user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        return this.users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }


}
