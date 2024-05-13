package com.fourchat.domain.models;


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
        this.sender = Mockito.mock(User.class);
        this.content = "Test content";
        this.creationDate = new Date();
        this.textMessage = new TextMessage(this.sender, this.content, this.creationDate);
    }

    @Test
    void testGetId() {
        this.textMessage.setId("123");
        assertEquals("123", this.textMessage.getId());
    }

    @Test
    void testGetSender() {
        assertEquals(this.sender, this.textMessage.getSender());
    }

    @Test
    void testGetContent() {
        assertEquals(this.content, this.textMessage.getContent());
    }

    @Test
    void testGetCreationDate() {
        assertEquals(this.creationDate, this.textMessage.getCreationDate());
    }

    @Test
    void testSetContent() {
        String newContent = "New test content";
        this.textMessage.setContent(newContent);
        assertEquals(newContent, this.textMessage.getContent());
    }

    @Test
    void testGetUpdated() {
        assertFalse(this.textMessage.getUpdated());
        this.textMessage.setUpdated(true);
        assertTrue(this.textMessage.getUpdated());
    }
}