package com.fourchat.application.adapters;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;
import com.fourchat.domain.ports.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BasicUser createBasicUser(String userName, String userEmail) {

        Optional<User> user = userRepository.findUserByUserName(userName);

        if (user.isPresent()) {
            return null;
        }

        BasicUser basicUser = new BasicUser(userName, userEmail);

        return (BasicUser) userRepository.save(basicUser);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {

        return userRepository.findUserByUserName(userName);
    }

    @Override
    public BasicUser getUserById(String userId) {
        return (BasicUser) userRepository.findById(userId).orElse(null);
    }
}
