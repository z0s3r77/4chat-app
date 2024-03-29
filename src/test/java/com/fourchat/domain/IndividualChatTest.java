package com.fourchat.domain;

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

class IndividualChatTest {

    private User user1;
    private User user2;
    private Message message1;
    private Message message2;
    private List<User> participants;
    private List<Message> messages;
    private Date creationDate;
    private IndividualChat individualChat;

    @BeforeEach
    public void setUp() {
        user1 = Mockito.mock(User.class);
        user2 = Mockito.mock(User.class);
        message1 = Mockito.mock(Message.class);
        message2 = Mockito.mock(Message.class);
        participants = new ArrayList<>();
        messages = new ArrayList<>();
        creationDate = new Date();
        individualChat = new IndividualChat(participants, new Date());
    }

    @Test
    void testGetParticipants() {
        participants.add(user1);
        participants.add(user2);
        assertEquals(participants, individualChat.getParticipants());
    }

    @Test
    void testAddParticipant() {
        individualChat.addParticipant(user1);
        assertTrue(individualChat.getParticipants().contains(user1));
    }

    @Test
    void testRemoveParticipant() {
        individualChat.addParticipant(user1);
        individualChat.removeParticipant(user1);
        assertFalse(individualChat.getParticipants().contains(user1));
    }

    @Test
    void testGetCreationDate() {
        assertEquals(creationDate, individualChat.getCreationDate());
    }

    @Test
    void testGetMessages() {
        messages.add(message1);
        messages.add(message2);
        assertEquals(messages, individualChat.getMessages());
    }

    @Test
    void testGetLastMessage() {
        messages.add(message1);
        messages.add(message2);
        assertEquals(message2, individualChat.getLastMessage());
    }

    @Test
    void testAddMessage() {
        individualChat.addMessage(message1);
        assertTrue(individualChat.getMessages().contains(message1));
    }

    @Test
    void testRemoveMessage() {
        individualChat.addMessage(message1);
        individualChat.removeMessage(message1);
        assertFalse(individualChat.getMessages().contains(message1));
    }

    @Test
    void testUpdateMessage() {
        individualChat.addMessage(message1);
        Mockito.when(message1.getId()).thenReturn(String.valueOf(1));
        Message messageUpdate = Mockito.mock(Message.class);
        Mockito.when(messageUpdate.getId()).thenReturn(String.valueOf(1));
        individualChat.updateMessage(messageUpdate);
        assertEquals(messageUpdate, individualChat.getMessages().getFirst());
    }

    @Test
    void testNotifyParticipants() {
        individualChat.addParticipant(user1);
        individualChat.addParticipant(user2);
        individualChat.notifyParticipants(message1);
        Mockito.verify(user1, Mockito.times(1)).onMessageReceived(individualChat, message1);
        Mockito.verify(user2, Mockito.times(1)).onMessageReceived(individualChat, message1);
    }
}