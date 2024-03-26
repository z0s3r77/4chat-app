package com.fourchat.domain.models;

import java.util.Date;
import java.util.List;

public interface Chat {

    public int getId();
    public void setId(int id);
    public List<User> getParticipants();
    public void addParticipant(User user);
    public void removeParticipant(User user);
    public Date getCreationDate();
    public List<Message> getMessages();
    public Message getLastMessage();
    public void addMessage(Message message);
    public void removeMessage(Message message);
    public void updateMessage(Message message);
    public void notifyParticipants(Message message);


}
