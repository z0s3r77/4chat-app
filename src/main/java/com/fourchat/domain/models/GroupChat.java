package com.fourchat.domain.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GroupChat implements Chat{

    private String id;
    private String groupName;
    private String description;
    private List<User> participants;
    private List<User> admins;
    private List<Message> messages;
    private Date creationDate;
    private int messageCount = 0;

    public GroupChat(String groupName, String description, List<User> participants, List<User> admins, Date creationDate) {
        this.groupName = groupName;
        this.description = description;
        this.participants = participants;
        this.admins = admins;
        this.creationDate = creationDate;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<User> getParticipants() {
        return this.participants;
    }

    @Override
    public void addParticipant(User user) {
        if (!this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    @Override
    public void removeParticipant(User user) {
        this.participants.remove(user);
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public Message getLastMessage() {
        return this.messages.getLast();
    }

    @Override
    public void addMessage(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }

        message.setId(String.valueOf(messageCount++));
        messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    @Override
    public void updateMessage(Message messageUpdate) {
        for (int i = 0; i < messages.size(); i++) {
            if (Objects.equals(messages.get(i).getId(), messageUpdate.getId())) {
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
