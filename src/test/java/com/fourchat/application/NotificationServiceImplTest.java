package com.fourchat.application;

import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.infrastructure.controllers.dtos.MessageDto;
import com.fourchat.infrastructure.controllers.mappers.MessageDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MessageDtoMapper messageDtoMapper;

    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_withTextMessage_sendsNotification() {
        String userId = "user123";
        Message textMessage = new TextMessage(any(), "Hello", new Date());
        MessageDto messageDto = new MessageDto();  // Crea una instancia de MessageDto        when(messageDtoMapper.messageDtoMapper(textMessage)).thenReturn(messageDto);
        when(messageDtoMapper.messageDtoMapper(textMessage)).thenReturn(messageDto);
        notificationServiceImpl.sendNotification(userId, textMessage);

        verify(messageDtoMapper).messageDtoMapper(textMessage);
        verify(messagingTemplate).convertAndSend("/topic/notifications/" + userId, messageDto);
    }

    @Test
    void sendNotification_withNonTextMessage_doesNotSendNotification() {
        String userId = "user123";
        Message nonTextMessage = mock(Message.class); // Mock a non-TextMessage type

        notificationServiceImpl.sendNotification(userId, nonTextMessage);

        verify(messageDtoMapper, never()).messageDtoMapper(any());
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
