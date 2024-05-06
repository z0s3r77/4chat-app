package com.fourchat.infrastructure.repository.mappers;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.NotificationSender;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.repository.documents.ChatDocument;
import com.fourchat.infrastructure.repository.documents.MessageDocument;
import com.fourchat.infrastructure.repository.mongodb.ChatDocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ChatDocumentMapperImpl {

    private final UserService userService;
    private final ChatDocumentRepository chatDocumentRepository;
    private final NotificationSender notificationSender;


    public ChatDocument toChatDocument(Chat chat) {


        if (chat instanceof IndividualChat) {

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

        } else if (chat instanceof GroupChat) {

            ChatDocument chatDocument;

            if (chat.getId() == null) {
                chatDocument = new ChatDocument();
            } else {
                chatDocument = chatDocumentRepository.findById(chat.getId()).orElse(new ChatDocument());
            }
            chatDocument.setId(chat.getId());
            List<String> participantsIds = chat.getParticipants().stream()
                    .map(User::getId)
                    .toList();

            chatDocument.setParticipantsIds(participantsIds);

            if (chat.getMessages() == null) {
                chat.setMessages(List.of());
            }

            List<MessageDocument> messages = chat.getMessages().stream()
                    .map(message -> new MessageDocumentMapperImpl(userService).toMessageDocument(message))
                    .toList();

            chatDocument.setMessages(messages);
            chatDocument.setCreationDate(chat.getCreationDate());
            chatDocument.setMessageCount(chat.getMessages().size());
            chatDocument.setType("group");
            chatDocument.setAdminsIds(((GroupChat) chat).getAdmins().stream()
                    .map(User::getId)
                    .toList());

            chatDocument.setDescription(((GroupChat) chat).getDescription());
            chatDocument.setGroupName(((GroupChat) chat).getGroupName());

            return chatDocument;


        } else {
            return null;
        }


    }


    public Chat toChat(ChatDocument chatDocument) {

        if (chatDocument.getType().equals("individual")) {

            IndividualChat individualChat = new IndividualChat();

            individualChat.setId(chatDocument.getId());
            individualChat.setCreationDate(chatDocument.getCreationDate());

            List<User> participants = chatDocument.getParticipantsIds().stream()
                    .map(userService::getUserById)
                    .map(user -> {
                        if (user instanceof BasicUser) {
                            ((BasicUser) user).setNotificationSender(notificationSender);
                        }
                        return user;
                    })
                    .collect(Collectors.toList());
            individualChat.setParticipants(participants);

            List<Message> messages = chatDocument.getMessages().stream()
                    .map(messageDocument -> new MessageDocumentMapperImpl(userService).toMessage(messageDocument))
                    .collect(Collectors.toList());
            individualChat.setMessages(messages);

            individualChat.setMessageCount(chatDocument.getMessageCount());

            return individualChat;

        } else if (chatDocument.getType().equals("group")) {

            GroupChat groupChat = new GroupChat();
            groupChat.setId(chatDocument.getId());
            groupChat.setCreationDate(chatDocument.getCreationDate());

            List<User> participants = chatDocument.getParticipantsIds().stream()
                    .map(userService::getUserById)
                    .map(user -> {
                        if (user instanceof BasicUser) {
                            ((BasicUser) user).setNotificationSender(notificationSender);
                        }
                        return user;
                    })
                    .collect(Collectors.toList());
            groupChat.setParticipants(participants);

            List<Message> messages = chatDocument.getMessages().stream()
                    .map(messageDocument -> new MessageDocumentMapperImpl(userService).toMessage(messageDocument))
                    .collect(Collectors.toList());
            groupChat.setMessages(messages);
            groupChat.setMessageCount(chatDocument.getMessageCount());
            groupChat.setAdmins(chatDocument.getAdminsIds().stream()
                    .map(userService::getUserById)
                    .collect(Collectors.toList()));

            groupChat.setDescription(chatDocument.getDescription());
            groupChat.setGroupName(chatDocument.getGroupName());

            return groupChat;
        } else {
            return null;
        }


    }
}
