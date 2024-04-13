package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;

import java.util.List;

public interface ChatService {

    List<Chat> getChatsFromUser(String userName);

    Chat sendMessage(String userNameSender, Message message, String userNameReceiver);

    void updateMessageInChat(String chatId, Message messageUpdated);

    boolean sendMessage(String chatId, Message message);
    boolean removeMessageFromChat(String chatId, String messageId);

    Chat createGroupChat(List<String> participantsIds, List<String> groupAdminIds, String groupName, String description);
    boolean updateGroupChatDescription(String chatId, String newDescription);

    boolean updateGroupChatName(String chatId, String newGroupName);

    boolean removeParticipantFromGroupChat(String chatId, String adminId, String userId);

    boolean makeParticipantAdmin(String chatId, String adminId, String userId);

    boolean removeParticipantFromAdmins(String chatId, String adminId, String adminIdToRemove);
    boolean addParticipantToGroupChat(String chatId, String adminId, String userId);
}