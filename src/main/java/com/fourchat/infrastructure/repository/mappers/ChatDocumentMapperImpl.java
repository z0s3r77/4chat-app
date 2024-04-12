package com.fourchat.infrastructure.repository.mappers;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.IndividualChat;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.repository.documents.ChatDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ChatDocumentMapperImpl{

    private final UserService userService;


    public ChatDocument toChatDocument(Chat chat) {


        if (chat instanceof IndividualChat){

            ChatDocument chatDocument = new ChatDocument();
            chatDocument.setId(chat.getId());

            List<String> participantsIds = ((IndividualChat) chat).getParticipants().stream()
                    .map(User::getId)
                    .toList();

            chatDocument.setParticipantsIds(participantsIds);

            return chatDocument;

        } else {
            return null;


        }


    }

    
    public Chat toChat(ChatDocument chatDocument) {
        return null;
    }
}
