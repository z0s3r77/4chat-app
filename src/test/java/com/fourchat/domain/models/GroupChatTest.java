package com.fourchat.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupChatTest {

    private GroupChat groupChat;
    private User user;
    private User admin;
    private Message message;

    @BeforeEach
    public void setUp() {
        List<User> participants = new ArrayList<>();
        List<User> admins = new ArrayList<>();
        user = Mockito.mock(User.class);
        admin = Mockito.mock(User.class);
        participants.add(user);
        admins.add(admin);
        groupChat = new GroupChat("Test Group", "Test Description", participants, admins, new Date());
        message = Mockito.mock(Message.class);
    }

    @Test
    void testGetId() {
        groupChat.setId("123");
        assertEquals("123", groupChat.getId());
    }

    @Test
    void testGetParticipants() {
        assertTrue(groupChat.getParticipants().contains(user));
    }

    @Test
    void testAddParticipant() {
        User newUser = Mockito.mock(User.class);
        groupChat.addParticipant(newUser);
        assertTrue(groupChat.getParticipants().contains(newUser));
    }

    @Test
    void testRemoveParticipant() {
        groupChat.removeParticipant(user);
        assertFalse(groupChat.getParticipants().contains(user));
    }



    @Test
    void testAddMessage() {
        groupChat.addMessage(message);
        assertTrue(groupChat.getMessages().contains(message));
    }

    @Test
    void testRemoveMessage() {
        groupChat.addMessage(message);
        groupChat.removeMessage(message);
        assertFalse(groupChat.getMessages().contains(message));
    }

    @Test
    void testUpdateMessage() {
        Message updatedMessage = Mockito.mock(Message.class);
        Mockito.when(message.getId()).thenReturn("1");
        Mockito.when(updatedMessage.getId()).thenReturn("1");
        groupChat.addMessage(message);
        groupChat.updateMessage(updatedMessage);
        assertEquals(updatedMessage, groupChat.getMessages().getFirst());
    }

    @Test
    void testGetGroupName() {
        assertEquals("Test Group", groupChat.getGroupName());
    }

    @Test
    void testSetGroupName() {
        groupChat.setGroupName("New Test Group");
        assertEquals("New Test Group", groupChat.getGroupName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Test Description", groupChat.getDescription());
    }

    @Test
    void testSetDescription() {
        groupChat.setDescription("New Test Description");
        assertEquals("New Test Description", groupChat.getDescription());
    }

    @Test
    void testGetAdmins() {
        assertTrue(groupChat.getAdmins().contains(admin));
    }




}