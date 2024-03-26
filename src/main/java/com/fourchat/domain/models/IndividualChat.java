package com.fourchat.domain.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndividualChat implements Chat{

    private int id;
    private List<User> participants;
    private List<Message> messages;
    private Date creationDate;
    private int messageCount = 0;

    public IndividualChat( List<User> participants) {

        this.participants = participants;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public List<User> getParticipants() {
        return participants;
    }

    @Override
    public void addParticipant(User user) {
        participants.add(user);
    }

    @Override
    public void removeParticipant(User user) {
        participants.remove(user);
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public Message getLastMessage() {
        return messages.getLast();
    }

    @Override
    public void addMessage(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        message.setId(messageCount++);
        messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    @Override
    public void updateMessage(Message messageUpdate) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == messageUpdate.getId()) {
                messages.set(i, messageUpdate);
                break;
            }
        }
    }

    @Override
    public void notifyParticipants(Message message) {
        for (User user : participants) {
            if (user.equals(message.getSender())) {
                continue;
            }
            user.onMessageReceived(this , message);
        }
    }
}
