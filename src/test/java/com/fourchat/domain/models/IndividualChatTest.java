package com.fourchat.domain.models;

import com.fourchat.domain.models.IndividualChat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class IndividualChatTest {

    private IndividualChat individualChat;
    private User user;
    private Message message;

    @BeforeEach
    public void setUp() {
        List<User> participants = new ArrayList<>();
        user = Mockito.mock(User.class);
        participants.add(user);
        individualChat = new IndividualChat(participants, new Date());
        message = Mockito.mock(Message.class);
    }

    @Test
    void testGetId() {
        individualChat.setId("123");
        assertEquals("123", individualChat.getId());
    }

    @Test
    void testGetParticipants() {
        assertTrue(individualChat.getParticipants().contains(user));
    }

    @Test
    void testAddParticipant() {
        User newUser = Mockito.mock(User.class);
        individualChat.addParticipant(newUser);
        assertTrue(individualChat.getParticipants().contains(newUser));
    }

    @Test
    void testRemoveParticipant() {
        individualChat.removeParticipant(user);
        assertFalse(individualChat.getParticipants().contains(user));
    }

    @Test
    void testGetMessages() {
        assertTrue(individualChat.getMessages().isEmpty());
    }

    @Test
    void testAddMessage() {
        individualChat.addMessage(message);
        assertTrue(individualChat.getMessages().contains(message));
    }

    @Test
    void testRemoveMessage() {
        individualChat.addMessage(message);
        individualChat.removeMessage(message);
        assertFalse(individualChat.getMessages().contains(message));
    }

    @Test
    void testUpdateMessage() {
        Message updatedMessage = Mockito.mock(Message.class);
        Mockito.when(message.getId()).thenReturn("1");
        Mockito.when(updatedMessage.getId()).thenReturn("1");
        individualChat.addMessage(message);
        individualChat.updateMessage(updatedMessage);
        assertEquals(updatedMessage, individualChat.getMessages().getFirst());
    }


}