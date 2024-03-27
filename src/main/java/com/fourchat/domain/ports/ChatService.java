package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;

import java.util.List;

public interface ChatService {

    List<Chat> getChats(User user);
    void removeChat(User user, Chat chat);
    void addChatGroupParticipant(Chat groupChat, User groupAdmin, User participant);
    Chat sendMessage(User sender, Message message, User receiver);
    void updateMessageInChat(String chatId, String messageId, Message messageUpdated);

    void sendMessage(Chat chat, Message message);

    Chat findChat(User user1, User user2);
    List<Chat> getUserChats(User user);


}