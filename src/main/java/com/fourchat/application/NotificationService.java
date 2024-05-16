package com.fourchat.application;

import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.domain.ports.NotificationSender;
import com.fourchat.infrastructure.controllers.mappers.MessageDtoMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class NotificationService implements NotificationSender {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageDtoMapper messageDtoMapper;

    public NotificationService(SimpMessagingTemplate messagingTemplate, MessageDtoMapper messageDtoMapper) {
        this.messagingTemplate = messagingTemplate;
        this.messageDtoMapper = messageDtoMapper;
    }

    @Override
    public void sendNotification(String userId, Message message) {

        if (message instanceof TextMessage){

            messagingTemplate.convertAndSend("/topic/notifications/" + userId, messageDtoMapper.messageDtoMapper(message));
        }
    }
}