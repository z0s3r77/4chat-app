package com.fourchat.infrastructure.controllers.dtos;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;

import java.util.Date;
import java.util.List;

public record GroupChatDto(String id,
                           String groupName,
                           String description,
                           List<User> participants,
                           List<User> admins,
                           List<Message> messages,
                           Date creationDate,
                           int messageCount,
                           List<String> deletedByUsers,
                           Message lastMessage) {
    public static List<GroupChatDto> from(List<Chat> chats) {


        return chats.stream()
                .map(chat -> new GroupChatDto(
                        chat.getId(),
                        chat.getGroupName(),
                        chat.getDescription(),
                        chat.getParticipants(),
                        chat.getAdmins(),
                        chat.getMessages(),
                        chat.getCreationDate(),
                        chat.getMessageCount(),
                        chat.getDeletedByUsers(),
                        chat.getLastMessage()
                ))
                .toList();
    }


    public static GroupChatDto from(Chat chat) {
        return new GroupChatDto(
                chat.getId(),
                chat.getGroupName(),
                chat.getDescription(),
                chat.getParticipants(),
                chat.getAdmins(),
                chat.getMessages(),
                chat.getCreationDate(),
                chat.getMessageCount(),
                chat.getDeletedByUsers(),
                chat.getLastMessage()
        );
    }
}
