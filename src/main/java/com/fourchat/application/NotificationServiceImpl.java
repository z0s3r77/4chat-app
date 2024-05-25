package com.fourchat.application;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.SystemTextMessage;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.domain.ports.NotificationService;
import com.fourchat.infrastructure.controllers.dtos.SimpleTextMessage;
import com.fourchat.infrastructure.controllers.mappers.MessageDtoMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageDtoMapper messageDtoMapper;

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate, MessageDtoMapper messageDtoMapper) {
        this.messagingTemplate = messagingTemplate;
        this.messageDtoMapper = messageDtoMapper;
    }

    @Override
    public void sendNotification(String userId, Message message) {

        if (message instanceof TextMessage){

            messagingTemplate.convertAndSend("/topic/notifications/" + userId, messageDtoMapper.messageDtoMapper(message));
        } else if (message instanceof SystemTextMessage){

            messagingTemplate.convertAndSend("/topic/notifications/" + userId, messageDtoMapper.messageDtoMapper(message));
        }

    }

    @Override
    public void sendNotification(Chat chat, Message message) {
        if (message instanceof TextMessage){

            messagingTemplate.convertAndSend("/topic/chat/"+chat.getId(), messageDtoMapper.messageDtoMapper(message));
        }else if (message instanceof SystemTextMessage){

            messagingTemplate.convertAndSend("/topic/chat/"+chat.getId(), messageDtoMapper.messageDtoMapper(message));
        }
    }
}