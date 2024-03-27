package com.fourchat.domain;

import com.fourchat.domain.models.TextMessage;
import com.fourchat.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest {

    private User sender;
    private String content;
    private Date creationDate;
    private TextMessage textMessage;

    @BeforeEach
    public void setUp() {
        sender = Mockito.mock(User.class);
        content = "Test content";
        creationDate = new Date();
        textMessage = new TextMessage(sender, content, creationDate);
    }

    @Test
    public void testGetId() {
        textMessage.setId(String.valueOf(1));
        assertEquals(1, textMessage.getId());
    }

    @Test
    public void testSetId() {
        textMessage.setId(String.valueOf(2));
        assertEquals(2, textMessage.getId());
    }

    @Test
    public void testGetSender() {
        assertEquals(sender, textMessage.getSender());
    }

    @Test
    public void testGetContent() {
        assertEquals(content, textMessage.getContent());
    }

    @Test
    public void testGetCreationDate() {
        assertEquals(creationDate, textMessage.getCreationDate());
    }
}