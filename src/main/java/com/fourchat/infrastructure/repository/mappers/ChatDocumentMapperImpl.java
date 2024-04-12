package com.fourchat.infrastructure.repository.mappers;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.IndividualChat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.repository.documents.ChatDocument;
import com.fourchat.infrastructure.repository.documents.MessageDocument;
import com.fourchat.infrastructure.repository.mongodb.ChatDocumentRepository;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ChatDocumentMapperImpl{

    private final UserService userService;
    private final ChatDocumentRepository chatDocumentRepository;


    public ChatDocument toChatDocument(Chat chat) {


        if (chat instanceof IndividualChat){

            ChatDocument chatDocument;

            if (chat.getId() == null) {
                chatDocument = new ChatDocument();
            } else {
                chatDocument = chatDocumentRepository.findById(chat.getId()).orElse(new ChatDocument());
            }



            chatDocument.setId(chat.getId());

            List<String> participantsIds = ((IndividualChat) chat).getParticipants().stream()
                    .map(User::getId)
                    .toList();

            chatDocument.setParticipantsIds(participantsIds);

            List<MessageDocument> messages = ((IndividualChat) chat).getMessages().stream()
                    .map(message -> new MessageDocumentMapperImpl(userService).toMessageDocument(message))
                    .toList();

            chatDocument.setMessages(messages);
            chatDocument.setCreationDate(chat.getCreationDate());

            chatDocument.setMessageCount(chat.getMessages().size());

            chatDocument.setType("individual");

            return chatDocument;

        } else {
            return null;


        }


    }

    
    public Chat toChat(ChatDocument chatDocument) {

        if (chatDocument.getType().equals("individual")){

            IndividualChat individualChat = new IndividualChat();

            individualChat.setId(chatDocument.getId());
            individualChat.setCreationDate(chatDocument.getCreationDate());

            List<User> participants = chatDocument.getParticipantsIds().stream()
                    .map(userService::getUserById)
                    .collect(Collectors.toList());
            individualChat.setParticipants(participants);

            List<Message> messages = chatDocument.getMessages().stream()
                    .map(messageDocument -> new MessageDocumentMapperImpl(userService).toMessage(messageDocument))
                    .collect(Collectors.toList());
            individualChat.setMessages(messages);

            individualChat.setMessageCount(chatDocument.getMessageCount());

            return individualChat;

        } else {
            return null;
        }


    }
}
