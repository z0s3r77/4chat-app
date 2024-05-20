package com.fourchat.application;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;
import com.fourchat.domain.ports.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BasicUser createBasicUser(String userName, String email, String firstName, String lastName) {

        Optional<User> user = userRepository.findUserByUserName(userName);

        if (user.isPresent()) {
            return (BasicUser) user.get();
        }

        BasicUser basicUser = new BasicUser(userName, email, firstName, lastName);

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

    @Override
    public List<User> autocompleteUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public boolean deleteUserFromContacts(String userId, String userIdToDelete) {

        BasicUser user = (BasicUser) userRepository.findById(userId).orElse(null);

        if (user == null) {
            return false;
        }

        if (user.removeContact(userIdToDelete)){
            userRepository.save(user);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
