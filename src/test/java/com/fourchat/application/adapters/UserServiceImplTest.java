package com.fourchat.application.adapters;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;
import com.fourchat.domain.ports.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository userRepositoryMock;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepositoryMock);
    }

    @Test
    void createBasicUser_UserExists_ReturnsNull() {
        String userName = "Carlos";
        String userEmail = "carlos@email.com";

        User existingUser = new BasicUser(userName, userEmail);
        when(userRepositoryMock.findUserByUserName(userName)).thenReturn(Optional.of(existingUser));

        BasicUser result = userService.createBasicUser(userName, userEmail);

        assertNull(result);
    }

    @Test
    void createBasicUser_UserDoesNotExist_CreatesNewUser() {
        String userName = "Carlos";
        String userEmail = "carlos@email.com";

        when(userRepositoryMock.findUserByUserName(userName)).thenReturn(Optional.empty());

        BasicUser newUser = new BasicUser(userName, userEmail);
        when(userRepositoryMock.save(any(BasicUser.class))).thenReturn(newUser);

        BasicUser result = userService.createBasicUser(userName, userEmail);

        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        assertEquals(userEmail, result.getEmail());
    }

    @Test
    void getUserByUserName_UserDoesNotExist_ReturnsEmpty() {
        String userName = "Carlos";

        when(userRepositoryMock.findUserByUserName(userName)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName(userName);

        assertTrue(result.isEmpty());
    }

    @Test
    void getUserByUserName_UserExists_ReturnsUser() {
        String userName = "Carlos";
        User existingUser = new BasicUser(userName, "carlos@email.com");

        when(userRepositoryMock.findUserByUserName(userName)).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.getUserByUserName(userName);

        assertTrue(result.isPresent());
        assertEquals(userName, result.get().getUserName());
    }

}