package com.fourchat.domain.models;

import java.util.Date;
import java.util.List;

public interface Chat {

    String getId();
    void setId(String id);
    List<User> getParticipants();
    void addParticipant(User user);
    void removeParticipant(User user);
    Date getCreationDate();
    List<Message> getMessages();
    Message getLastMessage();
    void setMessages(List<Message> messages);
    void addMessage(Message message);
    void removeMessage(Message message);
    void updateMessage(Message message);
    void notifyParticipants(Message message);
    List<String> getDeletedByUsers();
    void addDeletedByUser(String userId);
    void setDeletedByUsers(List<String> deletedByUsers);
    String getGroupName();
    String getDescription();
    List<User> getAdmins();
    int getMessageCount();

}
