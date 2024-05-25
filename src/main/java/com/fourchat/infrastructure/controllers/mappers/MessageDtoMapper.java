package com.fourchat.infrastructure.controllers.mappers;

import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.SystemTextMessage;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.infrastructure.controllers.dtos.MessageDto;
import com.fourchat.infrastructure.controllers.dtos.SimpleTextMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageDtoMapper {

    public MessageDto messageDtoMapper(Message message) {

        if (message == null) {
            return null;
        }

        if (message instanceof TextMessage){

            return MessageDto.builder()
                    .id(message.getId())
                    .sender(message.getSender().getUserName())
                    .sender_id(message.getSender().getId())
                    .content(message.getContent())
                    .creationDate(message.getCreationDate())
                    .updated(message.getUpdated())
                    .deleted(message.getDeleted())
                    .build();
        }

        if (message instanceof SystemTextMessage){

            return MessageDto.builder()
                    .id(message.getId())
                    .sender(message.getSender().getUserName())
                    .sender_id(message.getSender().getId())
                    .content(message.getContent())
                    .creationDate(message.getCreationDate())
                    .updated(message.getUpdated())
                    .deleted(message.getDeleted())
                    .build();
        }


        return new MessageDto();
    }

}
