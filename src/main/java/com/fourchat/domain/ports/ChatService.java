package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;

import java.util.List;

public interface ChatService {

    Chat getChatById(String chatId);

    List<Chat> getChatsIndexFromUser(String userId);

    List<Chat> getChatsFromUser(String userName);

    boolean userIsInChat(String chatId, String userId);

    Chat sendMessage(String userNameSender, Message message, String userNameReceiver);

    void updateMessageInChat(String chatId, Message messageUpdated);

    boolean sendMessage(String chatId, Message message);

    boolean removeMessageFromChat(String chatId, String messageId);

    boolean deleteChat(String chatId, String userId);

    Chat createGroupChat(List<String> participantsIds, List<String> groupAdminIds, String groupName, String description);

    boolean updateGroupChatDescription(String chatId, String newDescription);

    boolean updateGroupChatName(String chatId, String newGroupName);

    boolean removeParticipantFromGroupChat(String chatId, String adminId, String userId);

    boolean makeParticipantAdmin(String chatId, String adminId, String userId);

    boolean removeParticipantFromAdmins(String chatId, String adminId, String adminIdToRemove);

    boolean addParticipantToGroupChat(String chatId, String adminId, String userId);

    List<Chat> getGroupChatsFromUser(String userId);

}