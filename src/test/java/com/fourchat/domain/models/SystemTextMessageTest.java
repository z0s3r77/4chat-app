package com.fourchat.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SystemTextMessageTest {

    private SystemTextMessage systemTextMessage;
    private String content;
    private Date creationDate;

    @BeforeEach
    public void setUp() {
        content = "Test content";
        creationDate = new Date();
        systemTextMessage = new SystemTextMessage(content, creationDate);
    }

    @Test
    void testGetId() {
        systemTextMessage.setId("123");
        assertEquals("123", systemTextMessage.getId());
    }

    @Test
    void testGetSender() {
        User sender = systemTextMessage.getSender();
        assertEquals("System", sender.getUserName());
    }

    @Test
    void testGetContent() {
        assertEquals(content, systemTextMessage.getContent());
    }

    @Test
    void testGetCreationDate() {
        assertEquals(creationDate, systemTextMessage.getCreationDate());
    }

    @Test
    void testSetContent() {
        String newContent = "New test content";
        systemTextMessage.setContent(newContent);
        assertEquals(newContent, systemTextMessage.getContent());
    }

    @Test
    void testUpdated() {
        assertFalse(systemTextMessage.updated());
    }
}