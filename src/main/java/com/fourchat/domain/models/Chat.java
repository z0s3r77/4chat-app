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
    void addMessage(Message message);
    void removeMessage(Message message);
    void updateMessage(Message message);
    void notifyParticipants(Message message);


}
