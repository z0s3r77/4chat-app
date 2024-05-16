package com.fourchat.infrastructure.controllers.mappers;

import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.infrastructure.controllers.dtos.MessageDto;
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
                    .content(message.getContent())
                    .creationDate(message.getCreationDate())
                    .updated(message.getUpdated())
                    .deleted(message.getDeleted())
                    .build();
        }

        return new MessageDto();
    }

}
