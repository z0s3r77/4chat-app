package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.User;

import java.util.List;

public interface ChatService {

    List<Chat> getChatsFromUser(User user);

    Chat sendMessage(User sender, Message message, User receiver);

    void updateMessageInChat(String chatId, Message messageUpdated);

    boolean sendMessage(String chatId, Message message);

    Chat findChat(User user1, User user2);

    boolean removeMessageFromChat(String chatId, String messageId);

    Chat createGroupChat(List<User> participants, List<User> groupAdmin, String groupName, String description);

    boolean updateGroupChatDescription(String chatId, String newDescription);

    boolean updateGroupChatName(String chatId, String newGroupName);

    boolean removeParticipantFromGroupChat(String chatId, String adminName, String userName);

    boolean makeParticipantAdmin(String chatId, String adminName, String userName);

    boolean removeParticipantFromAdmins(String chatId, String adminName, String userName);
}