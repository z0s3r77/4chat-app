package com.fourchat.application;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBasicUser_userDoesNotExist_createsAndReturnsUser() {
        String userName = "user1";
        String userEmail = "user1@example.com";
        when(userRepository.findUserByUserName(userName)).thenReturn(Optional.empty());
        BasicUser newUser = new BasicUser(userName, userEmail, null, null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        BasicUser result = userService.createBasicUser(userName, userEmail, null, null);

        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        verify(userRepository).findUserByUserName(userName);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createBasicUser_userExists_returnsExistingUser() {
        String userName = "user1";
        String userEmail = "user1@example.com";
        BasicUser existingUser = new BasicUser(userName, userEmail, null, null);
        when(userRepository.findUserByUserName(userName)).thenReturn(Optional.of(existingUser));

        BasicUser result = userService.createBasicUser(userName, userEmail, null, null);

        assertNotNull(result);
        assertEquals(existingUser, result);
        verify(userRepository).findUserByUserName(userName);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByUserName_existingUser_returnsUser() {
        String userName = "user1";
        BasicUser existingUser = new BasicUser(userName, "user1@example.com", null, null);
        when(userRepository.findUserByUserName(userName)).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.getUserByUserName(userName);

        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
        verify(userRepository).findUserByUserName(userName);
    }

    @Test
    void getUserByUserName_nonExistingUser_returnsEmpty() {
        String userName = "user1";
        when(userRepository.findUserByUserName(userName)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName(userName);

        assertFalse(result.isPresent());
        verify(userRepository).findUserByUserName(userName);
    }

    @Test
    void getUserById_existingUser_returnsUser() {
        String userId = "user1";
        BasicUser existingUser = new BasicUser("user1", "user1@example.com", null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        BasicUser result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(existingUser, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void getUserById_nonExistingUser_returnsNull() {
        String userId = "user1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        BasicUser result = userService.getUserById(userId);

        assertNull(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void autocompleteUsersByName_existingUsers_returnsUsers() {
        String name = "user";
        List<User> users = List.of(new BasicUser("user1", "user1@example.com",null,null), new BasicUser("user2", "user2@example.com", null, null));
        when(userRepository.findByNameContainingIgnoreCase(name)).thenReturn(users);

        List<User> result = userService.autocompleteUsersByName(name);

        assertNotNull(result);
        assertEquals(users, result);
        verify(userRepository).findByNameContainingIgnoreCase(name);
    }

    @Test
    void save_user_returnsSavedUser() {
        User user = new BasicUser("user1", "user1@example.com", null, null);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).save(user);
    }
}
