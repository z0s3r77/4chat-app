package com.fourchat.domain.models;

import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class GroupChat implements Chat {

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
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void addMessage(Message message) {

        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }

        message.setId(String.valueOf(this.messageCount++));
        this.messages.add(message);
    }

    @Override
    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    @Override
    public void updateMessage(Message messageUpdate) {
        ListIterator<Message> iterator = this.messages.listIterator();
        while (iterator.hasNext()) {
            Message currentMessage = iterator.next();
            if (Objects.equals(currentMessage.getId(), messageUpdate.getId())) {
                iterator.set(messageUpdate);
                break;
            }
        }
    }

    @Override
    public void notifyParticipants(Message message) {

        this.participants.stream()
                .filter(user -> !user.equals(message.getSender()))
                .forEach(user -> user.onMessageReceived(this, message));
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

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }



    public void addAdmin(User admin) {
        User adminToAdd = this.participants.stream()
                .filter(user -> user.getId().equals(admin.getId()))
                .findFirst()
                .orElse(null);

        if (adminToAdd != null && !this.admins.contains(adminToAdd)) {
            this.admins.add(adminToAdd);
        }
    }

    public void removeAdmin(User admin) {
        User adminToRemove = this.admins.stream()
                .filter(adminToDelete -> admin.getId().equals(adminToDelete.getId()))
                .findFirst()
                .orElse(null);

        if (adminToRemove != null) {
            this.admins.remove(adminToRemove);
        }
    }

}
