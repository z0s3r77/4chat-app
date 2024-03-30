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
    private final Date creationDate;
    private int messageCount = 0;

    public GroupChat(String groupName, String description, List<User> participants, List<User> admins, Date creationDate) {
        this.groupName = groupName;
        this.description = description;
        this.participants = new ArrayList<>(participants);
        this.admins = new ArrayList<>(admins);
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
        List<User> updatedParticipants = new ArrayList<>();
        for (User participant : this.participants) {
            if (!participant.equals(user)) {
                updatedParticipants.add(participant);
            }
        }
        this.participants = updatedParticipants;
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

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getAdmins() {
        return this.admins;
    }

    public void addAdmin(User admin) {
        if (!this.admins.contains(admin)) {
            this.admins.add(admin);
        }
    }

    public void removeAdmin(User admin) {
        this.admins.remove(admin);
    }

}
