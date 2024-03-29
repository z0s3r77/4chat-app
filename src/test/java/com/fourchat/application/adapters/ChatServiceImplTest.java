package com.fourchat.application.adapters;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ChatServiceImplTest {

    ChatRepository chatRepositoryMock = Mockito.mock(ChatRepository.class);
    ChatServiceImpl chatService = new ChatServiceImpl(chatRepositoryMock);

    User user1 = new BasicUser("Carlos", "user1@email.com");
    User user2 = new BasicUser("Raul", "user2@email.com");



    @Test
    void createIndividualChat() {

        when(chatRepositoryMock.save(any(Chat.class))).thenAnswer(i -> {
            Chat chat = i.getArgument(0);
            chat.setId("123");
            return chat;
        });

        Mockito.verify(chatRepositoryMock).save(any(Chat.class));
    }

    @Test
    void sendMessage() {

        Message message = new TextMessage(user1, "Hello Raul", new Date());





    }


    @Test
    void findChat() {
        Chat chat = chatService.findChat(user1, user2);
        assertNull(chat, "Chat should be null");

        when(chatRepositoryMock.findAll()).thenReturn(Arrays.asList(new IndividualChat(Arrays.asList(user1, user2), new Date())));
        chat = chatService.findChat(user1, user2);
        assertNotNull(chat, "Chat should not be null");
    }


}