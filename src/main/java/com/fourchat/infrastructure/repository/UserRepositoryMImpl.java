package com.fourchat.infrastructure.repository;

import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;
import com.fourchat.infrastructure.repository.mappers.UserDocumentMapperImpl;
import com.fourchat.infrastructure.repository.mongodb.UserDocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryMImpl implements UserRepository {

    private final UserDocumentRepository userRepository;
    private final UserDocumentMapperImpl userDocumentMapper;

    @Override
    public Optional<User> findUserByUserName(String userName) {

        return userRepository.findByUserName(userName)
                .map(userDocumentMapper::toUser);
    }

    @Override
    public User save(User user) {
        return userDocumentMapper.toUser(userRepository.save(userDocumentMapper.toUserDocument(user)));
    }

    @Override
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId).map(userDocumentMapper::toUser);
    }
}
