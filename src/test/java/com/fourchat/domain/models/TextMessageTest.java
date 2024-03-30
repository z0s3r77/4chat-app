package com.fourchat.domain.models;


import com.fourchat.domain.models.TextMessage;
import com.fourchat.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest {

    private TextMessage textMessage;
    private User sender;
    private String content;
    private Date creationDate;

    @BeforeEach
    public void setUp() {
        sender = Mockito.mock(User.class);
        content = "Test content";
        creationDate = new Date();
        textMessage = new TextMessage(sender, content, creationDate);
    }

    @Test
    void testGetId() {
        textMessage.setId("123");
        assertEquals("123", textMessage.getId());
    }

    @Test
    void testGetSender() {
        assertEquals(sender, textMessage.getSender());
    }

    @Test
    void testGetContent() {
        assertEquals(content, textMessage.getContent());
    }

    @Test
    void testGetCreationDate() {
        assertEquals(creationDate, textMessage.getCreationDate());
    }

    @Test
    void testSetContent() {
        String newContent = "New test content";
        textMessage.setContent(newContent);
        assertEquals(newContent, textMessage.getContent());
    }

    @Test
    void testUpdated() {
        assertFalse(textMessage.updated());
        textMessage.setUpdated(true);
        assertTrue(textMessage.updated());
    }
}