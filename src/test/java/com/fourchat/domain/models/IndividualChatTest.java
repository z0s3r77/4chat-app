package com.fourchat.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndividualChatTest {

    private IndividualChat individualChat;
    private User user;
    private Message message;

    @BeforeEach
    public void setUp() {
        List<User> participants = new ArrayList<>();
        this.user = Mockito.mock(User.class);
        participants.add(this.user);
        this.individualChat = new IndividualChat(participants, new Date());
        this.message = Mockito.mock(Message.class);
    }

    @Test
    void testGetId() {
        this.individualChat.setId("123");
        assertEquals("123", this.individualChat.getId());
    }

    @Test
    void testGetParticipants() {
        assertTrue(this.individualChat.getParticipants().contains(this.user));
    }

    @Test
    void testAddParticipant() {
        User newUser = Mockito.mock(User.class);
        this.individualChat.addParticipant(newUser);
        assertTrue(this.individualChat.getParticipants().contains(newUser));
    }


    @Test
    void testGetMessages() {
        assertTrue(this.individualChat.getMessages().isEmpty());
    }

    @Test
    void testAddMessage() {
        this.individualChat.addMessage(this.message);
        assertTrue(this.individualChat.getMessages().contains(this.message));
    }

    @Test
    void testRemoveMessage() {
        this.individualChat.addMessage(this.message);
        this.individualChat.removeMessage(this.message);
        assertFalse(this.individualChat.getMessages().contains(this.message));
    }

    @Test
    void testUpdateMessage() {
        Message updatedMessage = Mockito.mock(Message.class);
        Mockito.when(this.message.getId()).thenReturn("1");
        Mockito.when(updatedMessage.getId()).thenReturn("1");
        this.individualChat.addMessage(this.message);
        this.individualChat.updateMessage(updatedMessage);
        assertEquals(updatedMessage, this.individualChat.getMessages().getFirst());
    }


}