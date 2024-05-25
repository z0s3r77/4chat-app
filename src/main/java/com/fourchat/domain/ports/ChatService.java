package com.fourchat.domain.ports;

import com.fourchat.domain.models.Chat;
import com.fourchat.domain.models.Message;
import com.fourchat.infrastructure.controllers.dtos.GroupDto;

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

    void updateGroupChatDescription(String chatId, String newDescription);

    void updateGroupChatName(String chatId, String newGroupName);

    void removeParticipantFromGroupChat(String chatId, String adminId, String userId);

    void makeParticipantAdmin(String chatId, String adminId, String userId);

    void removeParticipantFromAdmins(String chatId, String adminId, String adminIdToRemove);

    boolean exitFromGroupChat(String chatId, String userId);

    void addParticipantToGroupChat(String chatId, String adminId, String userId);

    List<Chat> getGroupChatsFromUser(String userId);

    boolean updateGroupChat(GroupDto groupDto,String adminId, String groupId);

}