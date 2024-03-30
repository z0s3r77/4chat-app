package com.fourchat.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserTest {

    private BasicUser basicUser;
    private final String userName = "TestUser";
    private final String email = "testuser@example.com";

    @BeforeEach
    public void setUp() {
        basicUser = new BasicUser(userName, email);
    }

    @Test
    void testGetUserName() {
        assertEquals(userName, basicUser.getUserName());
    }

    @Test
    void testGetEmail() {
        assertEquals(email, basicUser.getEmail());
    }

    @Test
    void testOnMessageReceived() {
        Chat chat = Mockito.mock(Chat.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(chat.getId()).thenReturn("123");
        Mockito.when(message.getSender()).thenReturn(basicUser);
        Mockito.when(message.getContent()).thenReturn("Test message");
        Mockito.when(message.getId()).thenReturn("1");

        basicUser.onMessageReceived(chat, message);

        // As the method onMessageReceived doesn't return anything and just prints to the console,
        // we can't directly test its output. However, we can verify that the methods we expect to be called on the mock objects were called.
        Mockito.verify(chat, Mockito.times(1)).getId();
        Mockito.verify(message, Mockito.times(1)).getSender();
        Mockito.verify(message, Mockito.times(1)).getContent();
        Mockito.verify(message, Mockito.times(1)).getId();
    }
}